package com.netto.client.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class JsonMapperUtil {

    private static ObjectMapper objectMapper ;
    
    @SuppressWarnings("deprecation")
	public static ObjectMapper getJsonMapper(){
        if(objectMapper==null){
            synchronized(JsonMapperUtil.class){
                if(objectMapper==null){
                    objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,false);
                    objectMapper.setSerializationInclusion(Include.NON_NULL);
                    //objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
                }
            }
        }
        
        return objectMapper;
        
    }
    
  
}
