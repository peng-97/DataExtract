package com.example.shape.Util;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FreemarkerUtil {

    public FreemarkerUtil(){
        throw new AssertionError();
    }



    public static void setXlsHeader(HttpServletResponse response, String fileName){
        response.reset();
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Cache-Control", "public");
        response.setContentType("applicatoin/octet-stream;charset=utf-8");
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    public static void setXlsHeaderLook(HttpServletResponse response, String type, String fileName){
        try {
        response.reset();
        boolean isinmage="jpg".equalsIgnoreCase(type)||"gif".equalsIgnoreCase(type)||"png".equalsIgnoreCase(type)||"jpeg".equalsIgnoreCase(type);
        if(isinmage){
            response.setContentType("image/"+type);
        }else if("pdf".equalsIgnoreCase(type)){
             response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        }else{
            response.setHeader("Expires", "0");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Cache-Control", "public");
            response.setContentType("applicatoin/octet-stream;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public static boolean deleteExcel(File file) {
       return  file.delete();
    }
}
