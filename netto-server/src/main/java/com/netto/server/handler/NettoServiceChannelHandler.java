package com.netto.server.handler;

import com.netto.server.message.NettoMessage;

import io.netty.channel.ChannelHandlerContext;

public interface NettoServiceChannelHandler {

    /**
     * on message received.
     * 
     * @param channel channel.
     * @param message message.
     */
    void received(ChannelHandlerContext ctx, NettoMessage message) throws Exception;
    
    
    void caught(ChannelHandlerContext ctx,Throwable cause);
}
