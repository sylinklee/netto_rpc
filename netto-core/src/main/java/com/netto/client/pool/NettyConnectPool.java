package com.netto.client.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.netto.client.channel.SyncChannel;
import com.netto.context.ServiceAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyConnectPool implements ConnectPool<SyncChannel> {
	private List<ServiceAddress> servers = new ArrayList<ServiceAddress>();
	private GenericObjectPool<SyncChannel> pool;
	private int timeout;

	public NettyConnectPool(List<ServiceAddress> servers, GenericObjectPoolConfig config) {
		this.servers = servers;
		pool = new GenericObjectPool<SyncChannel>(new NettyConnectPoolFactory(), config);

	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public List<ServiceAddress> getServers() {
		return servers;
	}

	@Override
	public SyncChannel getResource() {
		try {
			return this.pool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void release(SyncChannel resource) {
		if (resource == null)
			return;
		this.pool.returnObject(resource);
	}

	private class NettyConnectPoolFactory implements PooledObjectFactory<SyncChannel> {

		@Override
		public PooledObject<SyncChannel> makeObject() throws Exception {
			SyncChannel syncChannel = new SyncChannel();
			EventLoopGroup group = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel channel) throws Exception {

							ChannelPipeline pipeline = channel.pipeline();
							pipeline.addLast("readTimeoutHandler", syncChannel.getReadTimeoutHandler(timeout));
							pipeline.addLast("writeTimeoutHandler", syncChannel.getWriteTimeoutHandler(timeout));
							pipeline.addLast("framer", new LineBasedFrameDecoder(Integer.MAX_VALUE));
							pipeline.addLast("decoder", new StringDecoder());
							pipeline.addLast("encoder", new StringEncoder());
							pipeline.addLast("handler", syncChannel.getHandler());
						}

					});
			// 简单策略随机取服务器，没有考虑权重
			for (int i = 0; i < servers.size(); i++) {
				try {
					int index = new Random(System.currentTimeMillis()).nextInt(servers.size());
					ServiceAddress server = servers.get(index);
					ChannelFuture channelFuture = bootstrap.connect(server.getIp(), server.getPort());
					Channel channel = channelFuture.sync().channel();
					syncChannel.setChannelFuture(channelFuture);
					syncChannel.setChannel(channel);
					return new DefaultPooledObject<SyncChannel>(syncChannel);
				} catch (Exception e) {
					;
				}
			}
			throw new Exception("No server available!");
		}

		@Override
		public void destroyObject(PooledObject<SyncChannel> p) throws Exception {

		}

		@Override
		public boolean validateObject(PooledObject<SyncChannel> p) {
			return p.getObject().getChannel().isWritable();
		}

		@Override
		public void activateObject(PooledObject<SyncChannel> p) throws Exception {

		}

		@Override
		public void passivateObject(PooledObject<SyncChannel> p) throws Exception {

		}

	}
}
