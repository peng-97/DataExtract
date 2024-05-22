package com.example.shape.Util;

import com.alibaba.druid.filter.config.ConfigTools;

import java.util.HashMap;
import java.util.Map;

public class PasswordUtil {

    /**
     * 加密
     */
    public Map encryption(String password) throws Exception {
//        String password = "123456";
//        System.out.println("明文密码: " + password);
        String[] keyPair = ConfigTools.genKeyPair(512);
        //私钥
        String privateKey = keyPair[0];
        //公钥
        String publicKey = keyPair[1];
        //用私钥加密后的密文
        password = ConfigTools.encrypt(privateKey, password);
        Map<String,String> map=new HashMap<>();
        map.put("publicKey",publicKey);
        map.put("password",password);
        return  map;
    }

    /**
     * 解密
     */
    public String  decryption(String publicKey,String password) throws Exception {
        String decryptPassword = ConfigTools.decrypt(publicKey, password);
        return decryptPassword;
    }




}
