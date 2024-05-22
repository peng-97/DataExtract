package com.example.shape.Util;


import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CustomInterfaces {
    static final Logger logger = LoggerFactory.getLogger(CustomInterfaces.class);
    private static RestTemplate restTemplate;



    //业务流登录接口
    public Map login(String oaurl,String username) throws Exception {
        Map<String,Object> userinfoMap=new HashMap<>();
        HttpUtil httpUtil=new HttpUtil();
        // 加密数据（这个就是向服务提交的最终数据）
        String url=oaurl+"verify/v3/login.do";
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("username",username);
        params.add("password","Chinadci.123");
        params.add("appType",0);
        params.add("moreDetails",false);
        HttpEntity<MultiValueMap<String,Object>> resultEntity=new HttpEntity<MultiValueMap<String,Object>>(params,httpUtil.postForm());
        ResponseEntity<JSONObject> responseEntity =  restTemplate.postForEntity(url, resultEntity,JSONObject.class);
        JSONObject response = responseEntity.getBody();
        Map<String,Object> resultinfo=new HashMap<>();
//        logger.info("登录信息："+response.get("success")+","+response.getJSONObject("data").toString());
        if((boolean)response.get("success")){
            response= response.getJSONObject("data");
            //是否存在session信息
            if(response.has("session")){
                response=response.getJSONObject("session");
                resultinfo.put("userid",response.getString("userid"));
                resultinfo.put("token",response.getString("token"));
            }
        }else{
            logger.error("错误：登录失败。"+response.toString());
        }
        return resultinfo;
    }
















}
