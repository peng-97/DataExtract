package com.example.shape.Util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UtilDateProcessorUtil implements JsonValueProcessor {
    private String format = "yyyy-MM-dd HH:mm:ss";//自定义时间格式化的样式

    public UtilDateProcessorUtil(String format) {
        this.format = format;
    }

    public UtilDateProcessorUtil() {
    }

    @Override
    public Object processArrayValue(Object o, JsonConfig jsonConfig) {
        return o;
    }

    @Override
    public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
     String ret="";
       if(o instanceof java.util.Date){
           SimpleDateFormat sdf=new SimpleDateFormat(format);
           sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置北京时区
           ret=sdf.format(((Date)o).getTime());
        }
    return ret;
    }
}
