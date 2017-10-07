package com.netto.server.message;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.netto.core.util.Constants;

public class ArgsDeserializer extends StdDeserializer<Object[]> {
	private Map<String, ServiceMethodDesc> serviceMethodParameterTypesCache = new ConcurrentHashMap<String, ServiceMethodDesc>();
	private ObjectMapper mapper;

	public ArgsDeserializer(ObjectMapper mapper) {
		this(Object[].class, mapper);
	}

	protected ArgsDeserializer(Class<?> vc, ObjectMapper mapper) {
		super(vc);
		this.mapper = mapper;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object[] deserialize(NettoMessage message) throws IOException {
		JsonParser jp = this.mapper.getFactory().createParser(new String(message.getBody(), "utf-8"));
		DeserializationContext ctxt = ((DefaultDeserializationContext) this.mapper.getDeserializationContext())
				.createInstance(this.mapper.getDeserializationConfig(), jp, this.mapper.getInjectableValues());
		return this.deserialize(jp, new NettoDeserializationContext(ctxt, message.getHeaders()));
	}

	@Override
	public Object[] deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		boolean flag = ctxt instanceof NettoDeserializationContext;
		if (!flag) {
			throw new JsonParseException(jp, "ctxt isn't NettoDeserializationContext!");
		}

		Map<String, String> headers = (Map<String, String>) ((NettoDeserializationContext) ctxt).getNettoHeaders();
		ctxt = ((NettoDeserializationContext) ctxt).getContext();
		String methodKey = headers.get(Constants.SERVICE_HEADER) + "/" + headers.get(Constants.METHOD_HEADER) + "/";

		if (headers.containsKey(Constants.ARGSLEN_HEADER)) {
			methodKey = methodKey + headers.get(Constants.ARGSLEN_HEADER);
		}
		Type[] types = null;
		if (this.serviceMethodParameterTypesCache.containsKey(methodKey)) {
			ServiceMethodDesc desc = this.serviceMethodParameterTypesCache.get(methodKey);
			types = desc.types;
		} else {
			throw new JsonParseException(jp, "so such method:" + methodKey);
		}

		int length = types != null ? types.length : 0;
		int currentIndex = 0;
		Object[] args = new Object[length];

		JsonToken currentToken = null;
		while ((currentToken = jp.nextValue()) != null) {
			switch (currentToken) {
			case START_ARRAY:
				continue;
			case END_ARRAY:
				return args;
			default:
				if (currentIndex < length) {
					args[currentIndex] = ctxt.readValue(jp, mapper.getTypeFactory().constructType(types[currentIndex]));
					currentIndex++;
				}
			}

		}

		return args;
	}

	public boolean registerMethodParameterTypes(String service, Class<?> clazz) {

		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			Class<?> declaredClazz = method.getDeclaringClass();
			if (declaredClazz == Object.class) {
				continue;
			}

			Type[] parameterTypes = method.getGenericParameterTypes();
			String key = service + "/" + method.getName() + "/" + parameterTypes.length;
			this.serviceMethodParameterTypesCache.put(key, new ServiceMethodDesc(key, parameterTypes));

			String defaultKey = service + "/" + method.getName() + "/";
			this.serviceMethodParameterTypesCache.put(defaultKey, new ServiceMethodDesc(key, parameterTypes));
		}

		return false;

	}

	private class ServiceMethodDesc {
		@SuppressWarnings("unused")
		String serviceMethod;
		Type[] types;

		public ServiceMethodDesc(String serviceMethod, Type[] types) {
			super();
			this.serviceMethod = serviceMethod;
			this.types = types;
		}

	}

	private class NettoDeserializationContext extends DeserializationContext {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private DeserializationContext context;

		private Map<String, String> nettoHeaders;

		protected NettoDeserializationContext(DeserializationContext src, Map<String, String> nettoHeaders) {
			super(src);
			this.context = src;
			this.nettoHeaders = nettoHeaders;
		}

		public DeserializationContext getContext() {
			return context;
		}

		public Map<String, String> getNettoHeaders() {
			return nettoHeaders;
		}

		@Override
		public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> generator, ObjectIdResolver resolver) {
			return this.context.findObjectId(id, generator, resolver);
		}

		@Override
		public void checkUnresolvedObjectId() throws UnresolvedForwardReference {
			this.context.checkUnresolvedObjectId();
		}

		@Override
		public JsonDeserializer<Object> deserializerInstance(Annotated annotated, Object deserDef)
				throws JsonMappingException {
			return this.deserializerInstance(annotated, deserDef);
		}

		@Override
		public KeyDeserializer keyDeserializerInstance(Annotated annotated, Object deserDef)
				throws JsonMappingException {
			return this.keyDeserializerInstance(annotated, deserDef);
		}

	}

}
