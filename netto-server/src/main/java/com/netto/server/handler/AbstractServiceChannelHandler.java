package com.netto.server.handler;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.netto.core.context.ServiceResponse;
import com.netto.core.exception.NettoIOException;
import com.netto.core.exception.RemoteAccessException;
import com.netto.core.filter.InvokeMethodFilter;
import com.netto.core.message.NettoFrame;
import com.netto.core.util.Constants;
import com.netto.core.util.RandomStrGenerator;
import com.netto.core.util.SignatureVerifier;
import com.netto.server.bean.NettoServiceBean;
import com.netto.server.bean.ServiceBean;
import com.netto.server.bean.ServiceRequest;
import com.netto.server.desc.impl.ServiceDescApiImpl;
import com.netto.server.handler.proxy.ServiceProxy;
import com.netto.server.message.ArgsDeserializer;
import com.netto.server.message.NettoMessage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractServiceChannelHandler implements NettoServiceChannelHandler {

	protected static Logger logger = Logger.getLogger(AbstractServiceChannelHandler.class);
	private String serviceApp;
	private String serviceGroup;
	private Map<String, NettoServiceBean> serviceBeans;
	private List<InvokeMethodFilter> filters;

	private ArgsDeserializer argDeser;
	private ObjectMapper objectMapper;

	public AbstractServiceChannelHandler(String serviceApp, String serviceGroup,
			Map<String, NettoServiceBean> serviceBeans, List<InvokeMethodFilter> filters) {
		this.serviceApp = serviceApp;
		this.serviceGroup = serviceGroup;
		this.serviceBeans = serviceBeans;
		ServiceBean bean = new ServiceBean();
		bean.setRefName("$serviceDesc");
		NettoServiceBean serivceBean = new NettoServiceBean(bean,
				new ServiceDescApiImpl(this.serviceApp, this.serviceGroup, this.serviceBeans));
		this.serviceBeans.put("$serviceDesc", serivceBean);

		this.filters = filters;
		this.initJson();
	}

	@Override
	public void caught(ChannelHandlerContext ctx, Throwable cause) {
		try {
			ServiceResponse<String> resObj = new ServiceResponse<String>();
			resObj.setErrorMessage(cause.getMessage());
			StringWriter writer = new StringWriter();
			this.objectMapper.writeValue(writer, resObj);
			writer.write(Constants.PROTOCOL_REQUEST_DELIMITER);

			ctx.writeAndFlush(writer.toString());
		} catch (Throwable t) {
			logger.error("caught error failed ", t);
		}

	}

	private void initJson() {
		this.objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		objectMapper.setSerializationInclusion(Include.NON_NULL);
		// objectMapper.setSerializationInclusion(Include.NON_DEFAULT);

		// ServiceRequestJacksonDeserializer deserializer = new
		// ServiceRequestJacksonDeserializer(ServiceRequest.class,
		// objectMapper);
		this.argDeser = new ArgsDeserializer(this.objectMapper);
		// SimpleModule simpleModule = new SimpleModule();
		//
		// simpleModule.addDeserializer(Object[].class, this.argDeser);
		// objectMapper.registerModule(simpleModule);

		for (String key : serviceBeans.keySet()) {
			NettoServiceBean bean = serviceBeans.get(key);
			Class<?> clazz = bean.getObject().getClass();
			this.argDeser.registerMethodParameterTypes(key, clazz);
		}

	}

	public void sendResponse(ChannelHandlerContext ctx, ServiceResponse<?> resObj, int timeout)
			throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter writer = new StringWriter();
		this.objectMapper.writeValue(writer, resObj);
		writer.write(Constants.PROTOCOL_REQUEST_DELIMITER);

		ChannelFuture future = ctx.writeAndFlush(writer.toString());

		try {
			boolean success = future.await(timeout, TimeUnit.MILLISECONDS);

			if (future.cause() != null) {
				throw new NettoIOException("error response error ", future.cause());
			} else if (!success) {
				throw new NettoIOException("response error timeout");
			}
		} catch (InterruptedException e) {
			throw new RemoteAccessException("await Interrupted", e);
		} finally {

		}
	}

	private boolean verifySignature(NettoMessage message) throws Exception {
		Map<String, String> headers = message.getHeaders();

		String signatureStr = headers.get(NettoFrame.SIGNATURE_HEADER);

		if (signatureStr != null) {
			String[] signatureTokens = signatureStr.split(",");

			if (signatureTokens.length != 2) {

				logger.error("verified error," + signatureStr);
				return false;
			}

			String signature = signatureTokens[0];
			int saltIndex = Integer.parseInt(signatureTokens[1]);// salt序号

			/* 根据时间戳来解析签名 */
			String realSignature = RandomStrGenerator.extractRandomStrBasedTimestamp(signature,
					RandomStrGenerator.HOUR_TIMESTAMP);

			boolean verified = SignatureVerifier.verify(message.getBody(), saltIndex, realSignature);

			if (!verified) {
				logger.error("verified error," + signatureStr);
			}
			return verified;

		}

		return true;
	}

	public void handle(ChannelHandlerContext ctx, NettoMessage message) throws Exception {
		ServiceResponse<Object> resObj = new ServiceResponse<Object>();

		try {
			resObj.setSuccess(false);

			boolean verified = this.verifySignature(message);
			if (verified) {
				ServiceRequest reqObj = new ServiceRequest();
				reqObj.setServiceName(message.getHeaders().get(Constants.SERVICE_HEADER));
				reqObj.setMethodName(message.getHeaders().get(Constants.METHOD_HEADER));
				// ServiceRequest reqObj = objectMapper.readValue(new
				// String(message.getBody(), "utf-8"),
				// ServiceRequest.class);
				// Object[] args = objectMapper.readValue(new
				// String(message.getBody(), "utf-8"), Object[].class);
				Object[] args = this.argDeser.deserialize(message);
				reqObj.setArgs(args);
				if (serviceBeans.containsKey(reqObj.getServiceName())) {
					NettoServiceBean nettoBean = serviceBeans.get(reqObj.getServiceName());
					ServiceProxy proxy = new ServiceProxy(reqObj, nettoBean, filters);

					try {
						Object ret = proxy.callService();
						// resObj.setBody(objectMapper.writeValueAsString(ret));
						resObj.setRetObject(ret);
						resObj.setSuccess(true);

					} catch (Throwable t) {
						t = this.getTargetException(t);
						logger.error(t.getMessage(), t);
						resObj.setErrorMessage(t.getMessage());
					}
					this.sendResponse(ctx, resObj, nettoBean.getServiceBean().getTimeout());

				} else {
					resObj.setErrorMessage("service " + reqObj.getServiceName() + " is not exsist!");
					this.sendResponse(ctx, resObj, Constants.DEFAULT_TIMEOUT);
				}
			} else {
				resObj.setErrorMessage("service  is not verified!");
				this.sendResponse(ctx, resObj, Constants.DEFAULT_TIMEOUT);
			}

		} catch (RemoteAccessException re) {
			logger.error("error when reply request " + message, re);
		} catch (NettoIOException ne) {
			logger.error("io error when reply request " + message, ne);
		} catch (Throwable t) {
			logger.error("error when process request " + message, t);
			resObj.setErrorMessage("error when process request " + message);
			this.sendResponse(ctx, resObj, Constants.DEFAULT_TIMEOUT);
		}

	}

	private Throwable getTargetException(Throwable t) {

		while (t != null && t instanceof InvocationTargetException
				&& ((InvocationTargetException) t).getTargetException() != null) {
			t = ((InvocationTargetException) t).getTargetException();
		}
		return t;

	}

}
