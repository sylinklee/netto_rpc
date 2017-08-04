package com.netto.server;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.netto.server.handler.NettyServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer implements InitializingBean {
	private static Logger logger = Logger.getLogger(NettyServer.class);
	private int port = 12345;
	private Map<String, Object> serviceBeans;

	public NettyServer(int port, Map<String, Object> serviceBeans) {
		this.port = port;
		this.serviceBeans = serviceBeans;
	}

	public Map<String, Object> getServiceBeans() {
		return serviceBeans;
	}

	public void afterPropertiesSet() throws Exception {
		this.run();
	}

	private void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {

							ChannelPipeline p = ch.pipeline();
							p.addLast(new NettyServerHandler(serviceBeans));
						}
					});

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(this.port).sync(); // (7)

			logger.info("server bind port:" + this.port);

			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
