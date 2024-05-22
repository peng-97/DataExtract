package com.example.shape.Util;

import org.apache.http.client.methods.HttpGet;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class HttpUtil {
    /**
     * 定制系统请求头设置
     * @param token
     * @return
     */
    public HttpHeaders findHttpHeaders(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("Accept-Language", "zh-CN,zh;q=0.9");
        headers.add("Connection", "keep-alive");
        headers.add("Cookie","token="+token);
//        headers.add("Host", "172.29.3.224:8010");
        headers.add("refer", "");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        return  headers;
    }
    //定制系统请求头设置
    public HttpHeaders findHttpHeadersDontLog(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("Accept-Language", "zh-CN,zh;q=0.9");
        headers.add("Connection", "keep-alive");
//        headers.add("Cookie","token="+token);
//        headers.add("Host", "172.29.3.224:8010");
        headers.add("refer", "");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        return  headers;
    }

    //定制系统请求头设置
    public HttpHeaders postForm(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return  headers;
    }






    //请求头基本设置
    public HttpHeaders addHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return  headers;
    }

    //定制post提交的请求头

    public HttpHeaders findHttpHeadersPost(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("token",token);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return  headers;
    }



    //定制文件post提交的请求头

    public HttpHeaders findHttpHeadersFilePost(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("token",token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return  headers;
    }


    //定制系统请求头设置
    public HttpHeaders postFormAndToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("token",token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return  headers;
    }



    //定制get请求定制HttpGet设置
    public HttpGet findHttpHeaders(String  httpGeturl,String token){

        HttpGet httpGet = new HttpGet(httpGeturl);
        //設置httpGet的头部參數信息
        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Cookie", "token=" + token);
        httpGet.setHeader("Host", "172.29.3.224:8010");
        httpGet.setHeader("refer", "");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        return  httpGet;
    }


    public void test(){





    }





}
