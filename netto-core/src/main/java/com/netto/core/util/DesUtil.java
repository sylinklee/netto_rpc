package com.netto.core.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

public final class DesUtil {
	private static Logger logger=Logger.getLogger(DesUtil.class);
	private static  SecretKeyFactory keyFactory;
	private static Cipher cipher ;
	static {
		try {
			//创建一个密匙工厂，然后用它把DESKeySpec转换成
			keyFactory = SecretKeyFactory.getInstance("DES");
			//Cipher对象实际完成加密操作
    		cipher = Cipher.getInstance("DES");
		} catch (Exception e) {
			logger.error("DesUtil",e);
		}
	}
	private DesUtil()
	{
		
	}
	
	public static String encrypt(byte[] content, String password) {
    	try{
    		SecureRandom random = new SecureRandom();
    		DESKeySpec desKey = new DESKeySpec(password.getBytes());
    		SecretKey securekey = keyFactory.generateSecret(desKey);
    		//用密匙初始化Cipher对象
    		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
    		//现在，获取数据并加密
    		//正式执行加密操作
    		return  new String(cipher.doFinal(content));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
