package com.example.shape.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandowUtil {
    //随机产生验证码
    public String findRange(){
        StringBuffer buffer = new StringBuffer("0123456789");
        int range = buffer.length();
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < 6; j ++) {
            //生成随机字符
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return  sb.toString();
    }
}
