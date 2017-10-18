package com.netto.server.handler.impl;

import java.util.List;
import java.util.Map;

import com.netto.core.filter.InvokeMethodFilter;
import com.netto.server.bean.NettoServiceBean;
import com.netto.server.handler.AbstractServiceChannelHandler;
import com.netto.server.handler.NettoServiceChannelHandler;
import com.netto.server.message.NettoMessage;
import com.netto.service.desc.ServerDesc;

import io.netty.channel.ChannelHandlerContext;

public class SynchronousChannelHandler extends AbstractServiceChannelHandler implements NettoServiceChannelHandler {

	public SynchronousChannelHandler(ServerDesc serverDesc, Map<String, NettoServiceBean> serviceBeans,
			List<InvokeMethodFilter> filters) {
		super(serverDesc, serviceBeans, filters);
	}

	@Override
	public void received(ChannelHandlerContext ctx, NettoMessage message) throws Exception {
		this.handle(ctx, message);
	}

}
