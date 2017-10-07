package com.netto.server.message;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.netto.core.context.ServiceRequest;

public class ServiceRequestJacksonDeserializer extends StdDeserializer<ServiceRequest> {
    

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final ObjectMapper mapper ; 

    public ServiceRequestJacksonDeserializer(Class<?> vc,ObjectMapper mapper) {
        super(vc);
        this.mapper = mapper;
    }

    private Logger logger = Logger.getLogger(ServiceRequestJacksonDeserializer.class);

    private Map<String, ServiceMethodDesc> serviceMethodParameterTypesCache = new ConcurrentHashMap<String, ServiceMethodDesc>();

    // private static final ThreadLocal<ServiceMethodDesc> _localContext = new
    // ThreadLocal<>();

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

    public boolean registerMethodParameterTypes(String service, Class<?> clazz) {

        Method[] methods =   clazz.getMethods();
  
        for (Method method : methods) {
            Class<?> declaredClazz = method.getDeclaringClass();
            if(declaredClazz == Object.class){
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

    public boolean registerMethodParameterTypes(String service, String methodName, Type[] parameterTypes) {
        String key = service + "/" + methodName;

        if (this.serviceMethodParameterTypesCache.containsKey(key)) {
            return false;
        } else {
            this.serviceMethodParameterTypesCache.put(key, new ServiceMethodDesc(key, parameterTypes));
        }

        return true;
    }


    @Override
    public ServiceRequest deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String methodName = null;
        String serviceName = null;
        JsonToken currentToken = null;
        int argsLen = -1;
        List<Object> args = null;
        try{
        while ((currentToken = jp.nextValue()) != null) {
//            System.out.println(currentToken);
//            System.out.println(jp.getCurrentName());
            switch (currentToken) {
                case VALUE_STRING:
                    switch (jp.getCurrentName()) {
                        case "methodName":
                            methodName = jp.getText();
                            break;
                        case "serviceName":
                            serviceName = jp.getText();
                            break;
                        default:
                            break;
                    }
                    break; 
                case VALUE_NUMBER_INT:
                    if (jp.getCurrentName().equals("argsLength")) {
                        argsLen = jp.getValueAsInt();
                        break;    
                    }
                                         
                case START_ARRAY:
                    args = this.readArgs(jp, ctxt, serviceName, methodName, argsLen);
                default:
                    break;
            }
        }
        }
        catch(Throwable t){
            logger.error("error when parse",t);
            throw t;
        }
        ServiceRequest request = new ServiceRequest();
        request.setMethodName(methodName);
        request.setServiceName(serviceName);
        request.setArgs(args);
        return request;
    }
    
    
    private List<Object> readArgs(JsonParser jp, DeserializationContext ctxt,String serviceName,String methodName,int argsLen) throws IOException{
        String methodKey =  serviceName + "/" + methodName + "/" ;
        if(argsLen!=-1){
            methodKey = methodKey+argsLen;
        }
        Type[] types = null;
        if(this.serviceMethodParameterTypesCache.containsKey(methodKey)){
            ServiceMethodDesc desc = this.serviceMethodParameterTypesCache.get(methodKey);
            types = desc.types;
        }
        else{
            throw new JsonParseException(jp,"so such method:"+methodKey);
        }
        
        int length = types!=null?types.length:0;
        int currentIndex = 0;
        List<Object> args = new ArrayList<Object>(length);
        
        JsonToken currentToken = null;
        while ((currentToken = jp.nextValue()) != null) {
            switch(currentToken){
                case END_ARRAY:
                    if(jp.getCurrentName().equals("args"))
                        return args;
                default:
                    if(currentIndex<length){
                        args.add(ctxt.readValue(jp, mapper.getTypeFactory().constructType(types[currentIndex])));
                        currentIndex++;
                    }
            }
            
        }
        
        return args;
    }

}
