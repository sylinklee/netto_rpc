package com.netto.client.channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

public class SyncChannel {
	private ChannelHandler handler;
	private Channel channel;
	private ChannelFuture channelFuture;
	private CountDownLatch latch;
	private String result;
	private Exception exception;

	public SyncChannel() {
		handler = new SimpleChannelInboundHandler<String>() {

			@Override
			protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
				result = msg;
				latch.countDown();
			}

		};
	}

	public ChannelHandler getHandler() {
		return handler;
	}

	public ReadTimeoutHandler getReadTimeoutHandler(long timeout) {
		return new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS) {
			@Override
			protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
				ctx.close();
				exception = new Exception("read timeout!");
				latch.countDown();
			}
		};
	}

	public WriteTimeoutHandler getWriteTimeoutHandler(long timeout) {
		return new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS) {

			@Override
			protected void writeTimedOut(ChannelHandlerContext ctx) throws Exception {
				ctx.close();
				exception = new Exception("write timeout!");
				latch.countDown();
			}

		};
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void writeAndFlush(String data) throws Exception {
		if (this.latch != null) {
			if (this.latch.getCount() > 0)
				this.latch.countDown();
		}
		this.latch = new CountDownLatch(1);
		this.exception = null;
		if (this.channel == null) {
			this.channel = this.channelFuture.sync().channel();
		}
		this.channel.write(data);
		this.channel.flush();

	}

	public String readLine() throws Exception {
		try {
			this.latch.await();
		} catch (InterruptedException e) {
		}
		if (this.exception != null) {
			throw this.exception;
		}
		return this.result;
	}
}
