package com.netto.server.handler;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import com.google.gson.Gson;
import com.netto.context.ServiceRequest;
import com.netto.context.ServiceResponse;
import com.netto.filter.InvokeMethodFilter;
import com.netto.server.bean.ServiceBean;
import com.netto.server.desc.impl.ServiceDescApiImpl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = Logger.getLogger(NettyServerHandler.class);
	private static Gson gson = new Gson();
	private Map<String, NettoServiceBean> serviceBeans;
	private List<InvokeMethodFilter> filters;

	public NettyServerHandler(Map<String, NettoServiceBean> serviceBeans, List<InvokeMethodFilter> filters) {
		this.serviceBeans = serviceBeans;
		ServiceBean bean = new ServiceBean();
		bean.setRef("$serviceDesc");
		NettoServiceBean serivceBean = new NettoServiceBean(bean, new ServiceDescApiImpl(this.serviceBeans));
		this.serviceBeans.put("$serviceDesc", serivceBean);
		this.filters = filters;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, "UTF-8");
			ServiceRequest reqObj = gson.fromJson(body, ServiceRequest.class);
			ServiceResponse resObj = new ServiceResponse();
			if (this.serviceBeans.containsKey(reqObj.getServiceName())) {
				ServiceProxy proxy = new ServiceProxy(reqObj,
						this.serviceBeans.get(reqObj.getServiceName()).getObject(), this.filters);

				try {
					resObj.setSuccess(true);
					resObj.setBody(proxy.callService());
				} catch (Throwable t) {
					logger.error(t.getMessage(), t);
					resObj.setSuccess(false);
					resObj.setBody(t.getMessage());
				}
			} else {
				resObj.setSuccess(false);
				resObj.setBody("service " + reqObj.getServiceName() + " is not exsist!");
			}
			String response = gson.toJson(resObj) + "\r\n";
			ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
			encoded.writeBytes(response.getBytes());
			ctx.write(encoded);
			ctx.flush();
			return;
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}

	public static class NettoServiceBean implements FactoryBean<Object> {
		private ServiceBean serviceBean;

		private Object refBean;

		public NettoServiceBean(ServiceBean serviceBean, Object refBean) {
			this.serviceBean = serviceBean;
			this.refBean = refBean;
		}

		public ServiceBean getServiceBean() {
			return serviceBean;
		}

		@Override
		public Object getObject() {
			return this.refBean;
		}

		@Override
		public Class<?> getObjectType() {
			return this.refBean.getClass();
		}

		@Override
		public boolean isSingleton() {
			return true;
		}
	}

}
