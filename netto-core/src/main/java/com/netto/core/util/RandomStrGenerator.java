package com.netto.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class RandomStrGenerator {

    
    private final static char[] base = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    
    public final static int HOUR_TIMESTAMP = 0;
    
    public final static int DATE_TIMESTAMP = 1;
    
    public final static int SECOND_TIMESTAMP = 2;
    
    public final static int MINITUE_TIMESTAMP = 3;
    
    /**
     * 创建8位随机字符串
     * 
     * @return
     */
    public static String createRandomString(){
        return createRandomString(8);
    }
    
    
    public static String createRandomString(int length){
        StringBuffer strBuffer = new StringBuffer(length);
        Random random = ThreadLocalRandom.current();
        for(int i=0;i<length;i++){
            strBuffer.append(base[random.nextInt(base.length)]);
        }
        
        return strBuffer.toString();
    }
    
    
    public static String md5(String inStr) throws UnsupportedEncodingException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(inStr.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = md5.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return String.valueOf(md5StrBuff);
    }
    

    
    /**
     * 检查随机字符串是否合法,如果字符串的时间戳在上下一秒/分钟/小时/天之内都属于合法
     * 并把随机字符串从时间戳中解出来
     * 否则返回null
     * 
     * @param type
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String extractRandomStrBasedTimestamp(String randomStr,int type) throws UnsupportedEncodingException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.set(Calendar.MILLISECOND, 0);
        long timestampNow = 0;
        long timestampLast = 0;
        long timestampNext = 0;
        
        if(type==HOUR_TIMESTAMP){
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            timestampNow = calendar.getTimeInMillis();
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            timestampNext = calendar.getTimeInMillis();
            
            calendar.add(Calendar.HOUR_OF_DAY, -2);
            timestampLast = calendar.getTimeInMillis();
        }
        else if(type==SECOND_TIMESTAMP){
            timestampNow = calendar.getTimeInMillis();
            
            calendar.add(Calendar.SECOND, 1);
            timestampNext = calendar.getTimeInMillis();
            
            calendar.add(Calendar.SECOND, -2);
            timestampLast = calendar.getTimeInMillis();
        }
        else if(type==MINITUE_TIMESTAMP){
            calendar.set(Calendar.SECOND, 0);
            timestampNow = calendar.getTimeInMillis();
            
            calendar.add(Calendar.MINUTE, 1);
            timestampNext = calendar.getTimeInMillis();
            
            calendar.add(Calendar.MINUTE, -2);
            timestampLast = calendar.getTimeInMillis();
        }
        else if(type==DATE_TIMESTAMP){
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            timestampNow = calendar.getTimeInMillis();
            
            
            calendar.add(Calendar.DATE, 1);
            timestampNext = calendar.getTimeInMillis();
            
            calendar.add(Calendar.DATE, -2);
            timestampLast = calendar.getTimeInMillis();
        }
        
        
        String timestampNowStr = md5(String.valueOf(~timestampNow));
        String timestampLastStr = md5(String.valueOf(~timestampLast));
        String timestampNextStr = md5(String.valueOf(~timestampNext));
        
        if(randomStr.contains(timestampNowStr)){
            return randomStr.replace(timestampNowStr, "");
        }
        else if(randomStr.contains(timestampLastStr)){
            return randomStr.replace(timestampLastStr, "");
        }
        else if(randomStr.contains(timestampNextStr)){
            return randomStr.replace(timestampNextStr, "");
        }
        else{
            return null;
        }
    }
    
    /**
     * 根据当前时间(hour)戳创建时间戳串
     * 
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String createTimestampStr(int type) throws UnsupportedEncodingException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long timestamp = 0;
        calendar.set(Calendar.MILLISECOND, 0);
        
        if(type==HOUR_TIMESTAMP){
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            timestamp = calendar.getTimeInMillis();
        }
        else if(type==SECOND_TIMESTAMP){
            timestamp = calendar.getTimeInMillis();
        }
        else if(type==MINITUE_TIMESTAMP){
            calendar.set(Calendar.SECOND, 0);
            timestamp = calendar.getTimeInMillis();
        }
        else if(type==DATE_TIMESTAMP){
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            timestamp = calendar.getTimeInMillis();
        }
        
        /*取反操作*/
        String timestampStr = md5(String.valueOf(~timestamp));
        
        return timestampStr;
    }
    
    /**
     * 基于based创建混合字符串，新生成字符串肯定包含based
     * 
     * @param based
     * @param another
     * @return
     */
    public static String mixedUp(String based,String another){
        Random random = ThreadLocalRandom.current();
        int p = random.nextInt(another.length()-1);
        
        return another.substring(0,p)+based+another.substring(p);
    }
    
    /**
     * 根据当前时间(hour)戳创建字符串,包括8位随机字符串和时间戳
     * 
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String createRandomStringBasedOnTimestamp(int type) throws UnsupportedEncodingException{

        String timestampStr = createTimestampStr(type);
        
        
        Random random = ThreadLocalRandom.current();
        int p = random.nextInt(8-1);
        int length = 8+timestampStr.length();
        StringBuffer strBuffer = new StringBuffer(length);
        
        
        for(int i=0;i<length;i++){
            strBuffer.append(base[random.nextInt(base.length)]);
            if(i==p){
                strBuffer.append(timestampStr);
            }
        }
        return strBuffer.toString();
        
    }
}
