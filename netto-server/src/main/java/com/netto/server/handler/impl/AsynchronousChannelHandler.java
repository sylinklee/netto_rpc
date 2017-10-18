package com.netto.server.handler.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.netto.core.filter.InvokeMethodFilter;
import com.netto.server.NamedThreadFactory;
import com.netto.server.bean.NettoServiceBean;
import com.netto.server.handler.AbstractServiceChannelHandler;
import com.netto.server.handler.NettoServiceChannelHandler;
import com.netto.server.message.NettoMessage;
import com.netto.service.desc.ServerDesc;

import io.netty.channel.ChannelHandlerContext;

public class AsynchronousChannelHandler extends AbstractServiceChannelHandler implements NettoServiceChannelHandler {
	private static Logger logger = Logger.getLogger(AsynchronousChannelHandler.class);

	private Executor executor = null;

	public AsynchronousChannelHandler(ServerDesc serverDesc, Map<String, NettoServiceBean> serviceBeans,
			List<InvokeMethodFilter> filters) {
		this(serverDesc, serviceBeans, filters, Integer.MAX_VALUE, 256);
	}

	public AsynchronousChannelHandler(ServerDesc serverDesc, Map<String, NettoServiceBean> serviceBeans,
			List<InvokeMethodFilter> filters, int maxQueueSize, int numHandlerWorker) {
		super(serverDesc, serviceBeans, filters);

		executor = new ThreadPoolExecutor(numHandlerWorker, numHandlerWorker, 60000L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(maxQueueSize), new NamedThreadFactory("NettoHandlerThread", true));

	}

	@Override
	public void received(ChannelHandlerContext ctx, NettoMessage message) throws Exception {
		try {
			this.executor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						AsynchronousChannelHandler.this.handle(ctx, message);
					} catch (Throwable t) {
						logger.error("error when call handler ", t);
					}

				}

			});
		} catch (Throwable t) {
			logger.error("error when submit task ", t);
		}
	}

}
