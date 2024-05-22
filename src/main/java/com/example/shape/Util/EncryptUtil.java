package com.example.shape.Util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    public  String md5(String plainText){
        String encryStr = null;
        if (plainText != null || !"".equals(plainText)){
            try {
                byte[] ret = MessageDigest.getInstance("md5").digest(plainText.getBytes());
                String md5Code = new BigInteger(1,ret).toString(16);
                for (int i = 0;i<32-md5Code.length(); i++){
                    md5Code = "0"+md5Code;
                }
                encryStr = md5Code;
            }catch (NoSuchAlgorithmException  e){
                e.printStackTrace();
            }

        }
        return encryStr;
    }

}
