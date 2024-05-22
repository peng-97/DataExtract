package com.example.shape.Util;



import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 编码帮助类
 */
public class CodingUtil {
//MD5加密
    public  String MD5Base64(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");


        byte[] md5Bytes = md.digest(str.getBytes());
        return bytesToHexStringBase64(md5Bytes);
    }

    public String bytesToHexStringBase64(byte[] bytes){
        BASE64Encoder b64Encoder = new BASE64Encoder();
        return  b64Encoder.encode(bytes);
    }


    //MD5加密
    public  String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    //Base64加密
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public  String decryptBASE64(String key) throws Exception {
        byte[] b = null;
        String result = null;
        if(key != null){
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(key);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * BASE64加密
     * @return
     * @throws Exception
     */
    public  String encryptBASE64(String str) throws Exception {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if( b != null){
            s = new BASE64Encoder().encode(b);
        }
        return s;

    }





}
