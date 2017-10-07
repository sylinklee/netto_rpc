package com.netto.server.handler.impl;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.netto.core.exception.NettoDecoderException;
import com.netto.server.handler.NettoServiceChannelHandler;
import com.netto.server.message.NettoMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.TooLongFrameException;

public class NettyNettoMessageHandler extends SimpleChannelInboundHandler<NettoMessage> {
	private static Logger logger = Logger.getLogger(NettyNettoMessageHandler.class);

	private NettoServiceChannelHandler channelHandler;

	public NettyNettoMessageHandler(NettoServiceChannelHandler channelHandler) {

		this.channelHandler = channelHandler;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("channel error:" + cause.getMessage());
		if (cause instanceof NettoDecoderException || cause instanceof TooLongFrameException) {
			this.channelHandler.caught(ctx, cause);
		} else if (cause instanceof IOException) {
			ctx.close();
		}

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NettoMessage msg) throws Exception {

		this.channelHandler.received(ctx, msg);

		return;

	}

}
