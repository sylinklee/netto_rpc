package com.netto.server.message;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.netto.core.exception.NettoDecoderException;
import com.netto.core.message.NettoFrame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

public class NettoFrameDecoder extends ByteToMessageDecoder {

    private int maxRequestSize = 1024 * 1024;
    
    private Logger logger = Logger.getLogger(NettoFrameDecoder.class);

    public NettoFrameDecoder(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    @SuppressWarnings("unused")
	@Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1.判断in.readerIndex
        int idx = in.readerIndex();
        int wrtIdx = in.writerIndex();

        if (wrtIdx > maxRequestSize) {
            // buffer size exceeded maxObjectLength; discarding the complete
            // buffer.
            in.skipBytes(in.readableBytes());
            throw new TooLongFrameException(
                    "object length exceeds " + maxRequestSize + ": " + wrtIdx + " bytes discarded");
        }

        for (; idx < wrtIdx; idx++) {
            byte c = in.getByte(idx);
            if (Character.isWhitespace(c)) {
                in.skipBytes(1);
            } else {
                break;
            }
        }

        if (in.readableBytes() >= NettoFrame.HEADER_LENGTH) {
            byte[] headerInfo = new byte[NettoFrame.HEADER_LENGTH];
            Arrays.fill(headerInfo, (byte) ' ');

            in.getBytes(in.readerIndex(), headerInfo);
            String headerStr = new String(headerInfo, "UTF-8");
            if (headerStr.startsWith(NettoFrame.NETTO_HEADER_START)) {

                try {
                    String headerInfoContent = headerStr.substring(NettoFrame.NETTO_HEADER_START.length());
                    String[] headerSections = headerInfoContent.split("/");
                    if (headerSections.length == 3) {
                        String version = headerSections[0];
                        String headerContentSizeAsString = headerSections[1].trim();
                        String bodySizeAsString = headerSections[2].trim();
                        int headerContentSize = Integer.parseInt(headerContentSizeAsString);
                        int bodySize = Integer.parseInt(bodySizeAsString);
                        if (in.readableBytes() >= bodySize + headerContentSize + NettoFrame.HEADER_LENGTH) {
                            ByteBuf headerContent = in.retainedSlice(NettoFrame.HEADER_LENGTH, headerContentSize);
                            ByteBuf body = in.retainedSlice(NettoFrame.HEADER_LENGTH + headerContentSize, bodySize);
                            NettoFrame nettoMessage = new NettoFrame();
                            nettoMessage.setBodySize(bodySize);
                            nettoMessage.setBody(body);
                            nettoMessage.setHeaderContent(headerContent);
                            nettoMessage.setHeaderContentSize(headerContentSize);
                            out.add(nettoMessage);
                            in.readerIndex(NettoFrame.HEADER_LENGTH + headerContentSize + bodySize);
                        }
                    } else {
                        in.skipBytes(in.readableBytes());
                        throw new NettoDecoderException("header parse error:" + headerStr);
                    }
                } catch (Throwable t) {
                    logger.error("decode error:"+headerStr,t);
                    in.skipBytes(in.readableBytes());
                    throw new NettoDecoderException("decode error:" + headerStr);
                    
                }

            } else {

                in.skipBytes(in.readableBytes());
                throw new NettoDecoderException("header start error:" + headerStr);
            }

        }

    }

}
