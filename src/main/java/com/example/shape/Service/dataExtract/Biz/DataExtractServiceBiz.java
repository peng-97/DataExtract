package com.example.shape.Service.dataExtract.Biz;
import cn.hutool.core.date.DateTime;
import com.example.shape.Service.dataExtract.DataExtractService;
import com.example.shape.Util.PinyinUtil;
import com.example.shape.mapper.dcidms.dcidmsMapper;
import com.example.shape.mapper.sourceor.sourceorMapper;
import com.example.shape.mapper.sourcetop.sourcetopMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataExtractServiceBiz implements DataExtractService {
    @Autowired
    private sourceorMapper psourceorMapper;

    @Autowired
    private sourcetopMapper psourcetopMapper;

    @Autowired
    private dcidmsMapper pdcidmsMapper;

    private String xqtable = "sjgx_zrz_xq";
    private String mlptable = "sjgx_zrz_mlp";
    private String floortable = "sjgx_h_floor";
    private String zdtable = "sjgx_zdjbxx";
    private  String zrzjzdTable="sjgx_zrz_jzd";
    private String zrzzdJzdTable="SJGX_ZDJBXX_JZD";

    private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    //不动产
    @Override
    public   List<Map<String, Object>> getSource(String sql){
        if(sql== null || sql.isEmpty())
            return null;
        return  psourceorMapper.getsource(sql);
    }

    //顶层
    public List<Map<String, Object>> getSource1(String sql){
        if(sql== null || sql.isEmpty())
            return null;
        return  psourcetopMapper.getsource(sql);
    }

    @Override
    public boolean dataExtract(String type) {
        System.out.print(DateTime.now().toString() + ":数据抽取开始\n");
//        new Thread(() -> {
////            long threadId = Thread.currentThread().getId();
////
////            threadLocal.set(threadId); // 为当前线程设置ThreadLocal变量
//            try {
//                Thread.sleep(100);
//                xqDatainSert();
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//                System.out.print(DateTime.now().toString() + ":数据抽取失败\n");
//            }
//        }).start();
//
//        new Thread(() -> {
////            long threadId = Thread.currentThread().getId();
////            threadLocal.set(threadId); // 为当前线程设置ThreadLocal变量
//            try {
//                Thread.sleep(200);
//               doorplateInsert();
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//                System.out.print(DateTime.now().toString() + ":数据抽取失败\n");
//            }
//        }).start();
//
//        new Thread(() -> {
////            long threadId = Thread.currentThread().getId();
////            threadLocal.set(threadId); // 为当前线程设置ThreadLocal变量
//            try {
//                Thread.sleep(300);
//               zdatainsert();
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//                System.out.print(DateTime.now().toString() + ":数据抽取失败\n");
//            }
//        }).start();
//
//        new Thread(() -> {
////            long threadId = Thread.currentThread().getId();
////            threadLocal.set(threadId); // 为当前线程设置ThreadLocal变量
//            try {
//                Thread.sleep(400);
//                insertFlooorData();
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//                System.out.print(DateTime.now().toString() + ":数据抽取失败\n");
//            }
//        }).start();

        try {
           switch (type){
               case "0":
                   floormlpInsert();//门楼牌房屋 //最慢
                   break;
               case "1":
                   insertFlooorData1();//房屋
                   break;
               case "2":
                   xqDatainSert();//小区
                   break;
               case "3":
                   zdatainsert();//宗地
                   break;
               case  "4":
                   doorplateInsert1();//门楼牌小区
                   break;
               case "5":
                   floormlpInsertdc();//门楼牌房屋 //最慢
                   break;
               case "6":
                   insertFlooorDatadc();//房屋
                   break;
               case "7":
                   xqDatainSertdc();//小区
                   break;
               case "8":
                   zdatainsertdc();//宗地
                   break;
               case  "9":
                   doorplateInsertdc();//门楼牌
                   break;

           }
            System.out.print(DateTime.now().toString() + ":数据抽取完成\n");
            return true;
        } catch (Exception ex) {
            // 获取堆栈跟踪元素
            StackTraceElement[] stackTrace = ex.getStackTrace();
            // 打印异常的位置信息
            for (StackTraceElement element : stackTrace) {
                System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
            }
            return false;
        }

    }


    //小区
    public void xqDatainSert() {

        String insertField = "name,pyname,abbrname,addname,guid,state,zipcode,x,y,sources,bsm,handle,dh,zddm,coldate";
//        List<Map<String,Object>> lstData=psourceorMapper.testse();//
        List<Map<String, Object>> lstData = psourceorMapper.getxqData();//
        if (lstData.size() == 0) return;
        String nameField = "XQMC";//名称字段
        String BSMField = "BSM";
        String zlField = "LDZL";
        //将目标数据库中的数据查询出来，对比验证
//        List<Map<String, Object>> residentialDataList = pdcidmsMapper.selectRes(xqtable);//小区数据
        List<String> residentialValues = new ArrayList<>();//小区数据

        List<String> bsmList = new ArrayList<>();//标识码去重
        List<String> nameList=new ArrayList<>();//名称去重
        int index = 0;
        lstData.forEach(row -> {
            try {
                String str = "(";
                String name = row.get(nameField) != null ? row.get(nameField).toString() : "";
                String zlValue = row.get(zlField) != null ? row.get(zlField).toString() : "";
                String bsmValue = row.get(BSMField).toString();
                if (!name.isEmpty() && !nameList.contains(name)) {
                    bsmList.add(bsmValue);//
                    nameList.add(name);
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "name":
                            case "abbrname":  //地址、简称
                                fieldValue = name;
                                break;
                            case "addname":  //坐落 -》标准地址
                                fieldValue = zlValue;
                                break;
                            case "x":  //经
                                fieldValue = row.get("XZBZ").toString();
                                break;
                            case "y":  //纬度
                                fieldValue = row.get("YZBZ").toString();
                                break;
                            case "state":  //章台
                                fieldValue = "在用";
                                break;
                            case "guid":  //uuid
                                fieldValue = UUID.randomUUID().toString();
                                break;
                            case "zipcode":  //邮编
                                fieldValue = "415900";
                                break;
                            case "sources":
                                fieldValue = "常德市不动产登记中心";
                                break;
                            case "coldate":
                                fieldValue = LocalDate.now().toString();
                                break;
                            case "pyname":
                                fieldValue = new PinyinUtil().getFullPinyin(name);
                                break;
                            case "bsm":
                                fieldValue = bsmValue;//宗地编码字段

                                break;
                            case "handle":
                                fieldValue = "0";//表示没有没有编码过，从对方数据库里面获取的

                                break;
                            case "address":
                                fieldValue = zlValue;
                                break;
                            case "createdata":
                                fieldValue = row.get("CREATEDATE").toString();
                                break;
                            case "dh":
                                fieldValue = row.get("DH").toString();
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str += ")";
                    residentialValues.add(str);
                }
            } catch (Exception ex) {
                System.out.print(row);//某一行出错了，还能继续下去
                System.out.print("在处理数据的时候出错了");
                System.out.print(ex.toString() + "\n");
                // 获取堆栈跟踪元素
                StackTraceElement[] stackTrace = ex.getStackTrace();
                // 打印异常的位置信息
                for (StackTraceElement element : stackTrace) {
                    System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                }

            }
        });
        try {
            if (!residentialValues.isEmpty()) { //小区
                String strValues = "";
                for (String value : residentialValues) {
                    strValues = strValues + value + ",";
                }
                strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                pdcidmsMapper.insertData(xqtable, insertField, strValues);
            }
            System.out.print("小区数据抽取成功\n");
        } catch (Exception ex) {
            System.out.print("抽取小区数据出错，在插入数据的时候\n");
            System.out.print(ex.toString() + "\n");
            // 获取堆栈跟踪元素
            StackTraceElement[] stackTrace = ex.getStackTrace();
            // 打印异常的位置信息
            for (StackTraceElement element : stackTrace) {
                System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
            }
        }

    }

    //门牌数据插入  数据拆分选项验证
    public void doorplateInsert1() {
        String insertField = "clasid,name,pyname,abbrname,addname,guid,state,zipcode,x,y,sources,bsm,handle,dh,zddm,coldate";
        List<Map<String, Object>> lstData = psourceorMapper.getlpData();//
        List<String> barValues = new ArrayList<>();//门牌值
        List<String> bsmList = new ArrayList<>();//标识码去重
        String nameField = "XQMC";//名称字段
        String BSMField = "BSM";
        String zlField = "LDZL";

        //将目标数据库中的数据查询出来，对比验证
        List<Map<String, Object>> barDataList = pdcidmsMapper.getsource("select distinct bsm from sjgx_zrz_mlp");//门楼牌数据  不是户牌
        lstData.forEach(row -> {
            try {
                String str = "(";
                String name = row.get(nameField) != null ? row.get(nameField).toString() : "";
                String zlValue = row.get(zlField) != null ? row.get(zlField).toString() : "";
                //判断宗地编码判断是否已经抽取过了,抽取过了不在抽取
                String bsmValue = row.get(BSMField).toString();
                if (barDataList.stream().noneMatch(t -> t.get("bsm").toString().equals(bsmValue)) && !bsmList.contains(bsmValue)) {
                    bsmList.add(bsmValue);//
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "name":  //地址、简称
                                fieldValue = name + (row.get("DH") != null ? row.get("DH").toString() : "");
                                break;
                            case "abbrname":
                                fieldValue = name;
                                break;
                            case "addname":  //坐落 -》标准地址
                                fieldValue = zlValue;
                                break;
                            case "x":  //经
                                fieldValue = row.get("XZBZ").toString();
                                break;
                            case "y":  //纬度
                                fieldValue = row.get("YZBZ").toString();
                                break;
                            case "state":  //章台
                                fieldValue = "在用";
                                break;
                            case "guid":  //uuid
                                fieldValue = UUID.randomUUID().toString();
                                break;
                            case "zipcode":  //邮编
                                fieldValue = "415900";
                                break;
                            case "sources":
                                fieldValue = "常德市不动产登记中心";
                                break;
                            case "coldate":
                                fieldValue = LocalDate.now().toString();
                                break;
                            case "pyname":
                                fieldValue = new PinyinUtil().getFullPinyin(name);
                                break;
                            case "bsm":
                                fieldValue = bsmValue;//宗地编码字段

                                break;
                            case "handle":
                                fieldValue = "0";//表示没有没有编码过，从对方数据库里面获取的

                                break;
                            case "dh":
                                fieldValue = row.get("DH").toString();
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                            case "clasid":
                                fieldValue = "";
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str += ")";
                    barValues.add(str);
                }

            } catch (Exception ex) {
                System.out.print("抽取门楼数据出错，在处理数据的时候\n");
                System.out.print(row);
                System.out.print(ex.toString() + "\n");
                // 获取堆栈跟踪元素
                StackTraceElement[] stackTrace = ex.getStackTrace();
                // 打印异常的位置信息
                for (StackTraceElement element : stackTrace) {
                    System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                }
            }
        });

        try {
            if (!barValues.isEmpty()) {  // 门楼牌
                String strValues = "";
                for (String value : barValues) {
                    strValues = strValues + value + ",";
                }
                strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                pdcidmsMapper.insertData(mlptable, insertField, strValues);
                System.out.print("门楼牌数据抽取完成\n");
            }
        } catch (Exception ex) {
            System.out.print("抽取门楼牌数据出错，在插入数据的时候\n");
            System.out.print(ex.toString() + "\n");
            // 获取堆栈跟踪元素
            StackTraceElement[] stackTrace = ex.getStackTrace();
            // 打印异常的位置信息
            for (StackTraceElement element : stackTrace) {
                System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
            }
        }
    }

    //房屋作为户牌数据插入
    public  void floormlpInsert(){
        String insertField = "clasid,name,pyname,abbrname,addname,guid,state,zipcode,x,y,sources,bsm,handle,dh,zddm,coldate";
        String nameField = "XQMC";//名称字段
        String BSMField = "BSM";
        String zlField = "ZL";
        List<String> countLst=psourceorMapper.getTableCount("sjgx_h");//查询有多少数据
        if(countLst.isEmpty()) return;
        int count=Integer.parseInt(countLst.get(0));//总数
        int index=0;
        int sum=1;
        int pageSize=1000;
        while (sum<count){
            List<String> guidList = new ArrayList<>();//guid去重
            List<String> barValues = new ArrayList<>();//门牌值
            String Sql="select t.uuid,t.hh,t.zl,p.*,s.xzbz,s.yzbz from ( SELECT *  FROM ( SELECT t.*,ROW_NUMBER() OVER (ORDER BY uuid) rn  FROM sjgx_h t ) " +
                    "  WHERE rn BETWEEN "+sum+" AND "+ (sum+pageSize-1)+") t,sjgx_zrz p,sjgx_zrz_jzd s " +
                    "where t.zrzbsm=p.bsm and t.zrzbsm=s.bsm and s.xzbz is not null and s.yzbz is not null";
            List<Map<String, Object>> lstData=psourceorMapper.getsource(Sql);
            for (Map<String, Object> row : lstData) {
                try {
                    String str = "(";
                    String name = row.get(nameField) != null ? row.get(nameField).toString() : "";
                    String zlValue = row.get(zlField) != null ? row.get(zlField).toString() : "";
                    //判断宗地编码判断是否已经抽取过了,抽取过了不在抽取
                    //两个表都没有的情况下
                    String guidValue = row.get("UUID").toString();
                    if (!guidList.contains(guidValue)) { //目标表、已经插入的都不包含进去
                        guidList.add(guidValue);//后重复
                        String bsmValue=row.get("BSM").toString();
                        for (String field : insertField.split(",")) { //名称
                            String fieldValue = "";
                            switch (field) {
                                case "name":  //地址、简称
                                    String dh=row.get("DH")!=null?row.get("DH").toString():"";
                                    String hh=row.get("HH")!=null?row.get("HH").toString():"";
                                    fieldValue =name+dh+hh;
                                    break;
                                case "abbrname":
                                    fieldValue = name;
                                    break;
                                case "addname":  //坐落 -》标准地址
                                    fieldValue = zlValue;
                                    break;
                                case "x":  //经
                                    fieldValue = row.get("XZBZ").toString();
                                    break;
                                case "y":  //纬度
                                    fieldValue = row.get("YZBZ").toString();
                                    break;
                                case "state":  //章台
                                    fieldValue = "在用";
                                    break;
                                case "guid":  //uuid
                                    fieldValue = guidValue;
                                    break;
                                case "zipcode":  //邮编
                                    fieldValue = "415900";
                                    break;
                                case "sources":
                                    fieldValue = "常德市不动产登记中心";
                                    break;
                                case "coldate":
                                    fieldValue = LocalDate.now().toString();
                                    break;
                                case "pyname":
                                    fieldValue = new PinyinUtil().getFullPinyin(name);
                                    break;
                                case "bsm":
                                    fieldValue = bsmValue;//

                                    break;
                                case "handle":
                                    fieldValue = "0";//表示没有没有编码过，从对方数据库里面获取的

                                    break;
                                case "dh":
                                    fieldValue = row.get("DH") != null ? row.get("DH").toString() : "";
                                    break;
                                case "zddm":
                                    fieldValue = row.get("ZDDM")!=null? row.get("ZDDM").toString():"";
                                    break;
                                case "clasid":
                                    fieldValue = "2930";//户牌
                                    break;
                            }
                            str = str + "'" + fieldValue + "',";
                        }
                        str = str.substring(0, str.length() - 1);
                        str += ")";
                        barValues.add(str);
                    }

                } catch (Exception ex) {
                    System.out.print("抽取门楼拍数据出错，在处理房屋数据的时候\n");
                    System.out.print(row);
                    System.out.print(ex.toString() + "\n");
                    // 获取堆栈跟踪元素
                    StackTraceElement[] stackTrace = ex.getStackTrace();
                    // 打印异常的位置信息
                    for (StackTraceElement element : stackTrace) {
                        System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                    }
                }
            }
            if(!barValues.isEmpty()){
                try {
                    String strValues = "";
                    for (String value : barValues) {
                        strValues = strValues + value + ",";
                    }
                    strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                    pdcidmsMapper.insertData(mlptable, insertField, strValues);
                    System.out.print("门楼牌数据抽取完成" + sum + "\n");
                }catch (Exception ex){
                    System.out.print("抽取门楼牌数据出错，在插入房屋数据的时候\n");
                    System.out.print(ex.toString() + "\n");
                    // 获取堆栈跟踪元素
                    StackTraceElement[] stackTrace = ex.getStackTrace();
                    // 打印异常的位置信息
                    for (StackTraceElement element : stackTrace) {
                        System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                    }
                }finally {
                    barValues.clear();

                }
            }
            sum+=pageSize;
        }
    }

    //宗地信息抽取
    public void zdatainsert() {
        String insertField = "bsm,zddm,zdmj,zl,bdcdyh,xs,ys,handle";
        List<Map<String, Object>> lstData = psourceorMapper.getzdData();
        if (lstData.isEmpty()) return;
        List<String> values = new ArrayList<>();
        List<String> bsmList = new ArrayList<>();//标识码
        for (Map<String, Object> row : lstData) {
            try {
                String bsmValue = row.get("BSM").toString();
                if (bsmList.stream().noneMatch(t -> t.equals(bsmValue))) {//没有抽取过的筛选一下
                    bsmList.add(bsmValue);
                    String xs="";
                    String ys="";
                    //排序
                    List<Map<String, Object>> bsmC = lstData.stream().filter(t -> t.get("BSM").toString().equals(bsmValue)).collect(Collectors.toList());
                    if(bsmC.isEmpty() || bsmC.get(0).get("SXH")==null || bsmC.get(0).get("SXH").toString().isEmpty()) continue;;
                    bsmC.sort(Comparator.comparingInt(s -> Integer.parseInt(s.get("SXH").toString())));
                    for (Map<String, Object> p : bsmC) {
                        if(p.get("XZBZ")==null ) continue;;
                        xs=xs+p.get("XZBZ").toString()+";";
                        ys=ys+ p.get("YZBZ").toString()+";";
                    }
                    xs = xs.substring(0, xs.length() - 1);
                    ys = ys.substring(0, ys.length() - 1);
                    String str = "(";
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "bsm":
                                fieldValue = bsmValue;
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                            case "zl":
                                fieldValue = row.get("ZL") != null ? row.get("ZL").toString() : "";
                                break;
                            case "zdmj":
                                fieldValue = row.get("ZDMJ") != null ? row.get("ZDMJ").toString() : "";
                                break;
                            case "bdcdyh":
                                fieldValue = row.get("BDCDYH") != null ? row.get("BDCDYH").toString() : "";
                                break;
                            case "xs":
                                fieldValue = xs;
                                break;
                            case "ys":
                                fieldValue = ys;
                                break;
                            case "handle":
                                fieldValue = "0";
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str = str + ")";
                    values.add(str);
                }
            } catch (Exception ex) {
                System.out.print("抽取宗地数据出错，在处理数据的时候\n");
                System.out.print(row);
                System.out.print(ex.toString() + "\n");
                // 获取堆栈跟踪元素
                StackTraceElement[] stackTrace = ex.getStackTrace();
                // 打印异常的位置信息
                for (StackTraceElement element : stackTrace) {
                    System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                }
            }
        }
        if(values.size()==0){
            System.out.print("宗地数据抽取完成\n");
            return;
        }
        //值
        try {
            String strValues = "";
            for (String value : values) {
                strValues = strValues + value + ",";
            }
            strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
            pdcidmsMapper.insertData(zdtable, insertField, strValues);
            System.out.print("宗地数据抽取完成\n");
        } catch (Exception ex) {
            System.out.print("抽取宗地数据出错，在插入数据的时候\n");
            System.out.print(ex.toString() + "\n");
        }
    }


    //循环  房屋
    public void insertFlooorData1() {
        String insertField = "guid,zddm,hh,zl,zrzh,bdcdyh,zt,zrzbsm";
        List<String> countLst=psourceorMapper.getTableCount("sjgx_h");//源数据库
        if(countLst.isEmpty()) return;
        Integer count=Integer.parseInt(countLst.get(0));//总数
        int sum=1;
        int pageSize=10000;
        while (sum<count){
            List<String> values = new ArrayList<>();
            List<Map<String, Object>> lstData=psourceorMapper.getFloorDataPage(Integer.toString(sum),Integer.toString((sum+pageSize-1)));
            for (Map<String, Object> row : lstData) {
                try {
                    String str = "(";
                    String guidValue = row.get("UUID").toString();//唯一标识
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "guid":
                                fieldValue = guidValue;
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                            case "zl":
                                fieldValue = row.get("ZL") != null ? row.get("ZL").toString() : "";
                                break;
                            case "hh":
                                fieldValue = row.get("HH") != null ? row.get("HH").toString() : "";
                                break;
                            case "zrzh":
                                fieldValue = row.get("ZRZH") != null ? row.get("ZRZH").toString() : "";
                                break;
                            case "bdcdyh":
                                fieldValue = row.get("BDCDYH") != null ? row.get("BDCDYH").toString() : "";
                                break;
                            case "zt":
                                fieldValue = row.get("ZT") != null ? row.get("ZT").toString() : "";
                                break;
                            case "zrzbsm":
                                fieldValue = row.get("ZRZBSM") != null ? row.get("ZRZBSM").toString() : "";
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str = str + ")";
                    values.add(str);

                } catch (Exception ex) {
                    System.out.print("抽取房屋数据出错，在处理房屋数据的时候\n");
                    System.out.print(row);
                    System.out.print(ex.toString() + "\n");
                    // 获取堆栈跟踪元素
                    StackTraceElement[] stackTrace = ex.getStackTrace();
                    // 打印异常的位置信息
                    for (StackTraceElement element : stackTrace) {
                        System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                    }
                }
            }
            try {
                if(values.isEmpty()) return;
                String strValues = "";
                for (String value : values) {
                    strValues = strValues + value + ",";
                }
                strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                pdcidmsMapper.insertData(floortable, insertField, strValues);
                System.out.print("房屋数据抽取"+sum+"\n");
            } catch (Exception ex) {
                System.out.print("抽取房屋数据出错，在插入数据的时候\n");
            }
            finally {

            }
            sum+=pageSize;
        }

    }


     ///鼎城  小区
    public void xqDatainSertdc() {
        String insertField = "name,pyname,abbrname,addname,guid,state,zipcode,x,y,sources,bsm,handle,dh,zddm,coldate";
        List<Map<String, Object>> lstData = psourcetopMapper.getxqData();//
        if (lstData.isEmpty()) return;
        String nameField = "XQMC";//名称字段
        String BSMField = "BSM";
        String zlField = "LDZL";
        List<String> residentialValues = new ArrayList<>();//小区数据
        List<String> nameList=new ArrayList<>();//名称去重
        int index = 0;
        lstData.forEach(row -> {
            try {
                String str = "(";
                String name = row.get(nameField) != null ? row.get(nameField).toString() : "";
                String zlValue = row.get(zlField) != null ? row.get(zlField).toString() : "";
                String bsmValue = row.get(BSMField).toString();
                if (!name.isEmpty() && !nameList.contains(name)) {

                    nameList.add(name);
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "name":
                            case "abbrname":  //地址、简称
                                fieldValue = name;
                                break;
                            case "addname":  //坐落 -》标准地址
                                fieldValue = zlValue;
                                break;
                            case "x":  //经

                                fieldValue = row.get("XZBZ").toString().split(",")[0];
                                break;
                            case "y":  //纬度
                                fieldValue = row.get("YZBZ").toString().split(",")[0];
                                break;
                            case "state":  //章台
                                fieldValue = "在用";
                                break;
                            case "guid":  //uuid
                                fieldValue = UUID.randomUUID().toString();
                                break;
                            case "zipcode":  //邮编
                                fieldValue = "415900";
                                break;
                            case "sources":
                                fieldValue = "鼎城不动产登记中心";
                                break;
                            case "coldate":
                                fieldValue = LocalDate.now().toString();
                                break;
                            case "pyname":
                                fieldValue = new PinyinUtil().getFullPinyin(name);
                                break;
                            case "bsm":
                                fieldValue = bsmValue;//宗地编码字段

                                break;
                            case "handle":
                                fieldValue = "0";//表示没有没有编码过，从对方数据库里面获取的

                                break;
                            case "address":
                                fieldValue = zlValue;
                                break;
                            case "createdata":
                                fieldValue = row.get("CREATEDATE").toString();
                                break;
                            case "dh":
                                fieldValue = row.get("DH").toString();
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str += ")";
                    residentialValues.add(str);
                }
            } catch (Exception ex) {
                System.out.print(row);//某一行出错了，还能继续下去
                System.out.print("在处理数据的时候出错了");
                System.out.print(ex.toString() + "\n");
                // 获取堆栈跟踪元素
                StackTraceElement[] stackTrace = ex.getStackTrace();
                // 打印异常的位置信息
                for (StackTraceElement element : stackTrace) {
                    System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                }
            }
        });
        try {
            if (!residentialValues.isEmpty()) { //小区
                String strValues = "";
                for (String value : residentialValues) {
                    strValues = strValues + value + ",";
                }
                strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                pdcidmsMapper.insertData(xqtable, insertField, strValues);
            }
            System.out.print("小区数据抽取成功\n");
        } catch (Exception ex) {
            System.out.print("抽取小区数据出错，在插入数据的时候\n");
            System.out.print(ex.toString() + "\n");
            // 获取堆栈跟踪元素
            StackTraceElement[] stackTrace = ex.getStackTrace();
            // 打印异常的位置信息
            for (StackTraceElement element : stackTrace) {
                System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
            }
        }
    }


    //宗地信息抽取 鼎城
    public void zdatainsertdc() {
        String insertField = "bsm,zddm,zdmj,zl,bdcdyh,xs,ys,handle";
        List<Map<String, Object>> lstData =psourcetopMapper.getzdData();
        if (lstData.isEmpty()) return;
        List<String> values = new ArrayList<>();
        List<String> bsmList = new ArrayList<>();//标识码
        for (Map<String, Object> row : lstData) {
            try {
                String bsmValue = row.get("BSM").toString();
                if (bsmList.stream().noneMatch(t -> t.equals(bsmValue))) {//没有抽取过的筛选一下
                    bsmList.add(bsmValue);
                    String xs=row.get("XZBZ").toString().replace(",",";");
                    String ys=row.get("YZBZ").toString().replace(",",";");
                    String str = "(";
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "bsm":
                                fieldValue = bsmValue;
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                            case "zl":
                                fieldValue = row.get("ZL") != null ? row.get("ZL").toString() : "";
                                break;
                            case "zdmj":
                                fieldValue = row.get("ZDMJ") != null ? row.get("ZDMJ").toString() : "";
                                break;
                            case "bdcdyh":
                                fieldValue = row.get("BDCDYH") != null ? row.get("BDCDYH").toString() : "";
                                break;
                            case "xs":
                                fieldValue = xs;
                                break;
                            case "ys":
                                fieldValue = ys;
                                break;
                            case "handle":
                                fieldValue = "0";
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str = str + ")";
                    values.add(str);
                }
            } catch (Exception ex) {
                System.out.print("抽取宗地数据出错，在处理数据的时候\n");
                System.out.print(row);
                System.out.print(ex.toString() + "\n");
                // 获取堆栈跟踪元素
                StackTraceElement[] stackTrace = ex.getStackTrace();
                // 打印异常的位置信息
                for (StackTraceElement element : stackTrace) {
                    System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                }
            }
        }
        //值
        try {
            String strValues = "";
            for (String value : values) {
                strValues = strValues + value + ",";
            }
            strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
            pdcidmsMapper.insertData(zdtable, insertField, strValues);
            System.out.print("宗地数据抽取完成\n");
        } catch (Exception ex) {
            System.out.print("抽取宗地数据出错，在插入数据的时候\n");
            System.out.print(ex.toString() + "\n");
        }
    }


    //门牌数据插入   鼎城
    public void doorplateInsertdc() {
        String insertField = "clasid,name,pyname,abbrname,addname,guid,state,zipcode,x,y,sources,bsm,handle,dh,zddm,coldate";
        List<Map<String, Object>> lstData = psourcetopMapper.getlpData();//
        List<String> barValues = new ArrayList<>();//门牌值
        List<String> bsmList = new ArrayList<>();//标识码去重
        String nameField = "XQMC";//名称字段
        String BSMField = "BSM";
        String zlField = "LDZL";

        //将目标数据库中的数据查询出来，对比验证
        List<Map<String, Object>> barDataList = pdcidmsMapper.getsource("select distinct bsm from sjgx_zrz_mlp");//门楼牌数据  不是户牌
        lstData.forEach(row -> {
            try {
                String str = "(";
                String name = row.get(nameField) != null ? row.get(nameField).toString() : "";
                String zlValue = row.get(zlField) != null ? row.get(zlField).toString() : "";
                //判断宗地编码判断是否已经抽取过了,抽取过了不在抽取
                String bsmValue = row.get(BSMField).toString();
                if (barDataList.stream().noneMatch(t -> t.get("bsm").toString().equals(bsmValue)) && !bsmList.contains(bsmValue)) {
                    bsmList.add(bsmValue);//
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "name":  //地址、简称
                                fieldValue = name + (row.get("DH") != null ? row.get("DH").toString() : "");
                                break;
                            case "abbrname":
                                fieldValue = name;
                                break;
                            case "addname":  //坐落 -》标准地址
                                fieldValue = zlValue;
                                break;
                            case "x":  //
                                fieldValue = row.get("XZBZ").toString().split(",")[0];
                                break;
                            case "y":  //
                                fieldValue = row.get("YZBZ").toString().split(",")[0];
                                break;
                            case "state":  //章台
                                fieldValue = "在用";
                                break;
                            case "guid":  //uuid
                                fieldValue = UUID.randomUUID().toString();
                                break;
                            case "zipcode":  //邮编
                                fieldValue = "415900";
                                break;
                            case "sources":
                                fieldValue = "鼎城不动产登记中心";
                                break;
                            case "coldate":
                                fieldValue = LocalDate.now().toString();
                                break;
                            case "pyname":
                                fieldValue = new PinyinUtil().getFullPinyin(name);
                                break;
                            case "bsm":
                                fieldValue = bsmValue;//宗地编码字段

                                break;
                            case "handle":
                                fieldValue = "0";//表示没有没有编码过，从对方数据库里面获取的

                                break;
                            case "dh":
                                fieldValue = row.get("DH").toString();
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                            case "clasid":
                                fieldValue = "";
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str += ")";
                    barValues.add(str);
                }

            } catch (Exception ex) {
                System.out.print("抽取门楼数据出错，在处理数据的时候\n");
                System.out.print(row);
                System.out.print(ex.toString() + "\n");
                // 获取堆栈跟踪元素
                StackTraceElement[] stackTrace = ex.getStackTrace();
                // 打印异常的位置信息
                for (StackTraceElement element : stackTrace) {
                    System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                }
            }
        });

        try {
            if (!barValues.isEmpty()) {  // 门楼牌
                String strValues = "";
                for (String value : barValues) {
                    strValues = strValues + value + ",";
                }
                strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                pdcidmsMapper.insertData(mlptable, insertField, strValues);
                System.out.print("门楼牌数据抽取完成\n");
            }
        } catch (Exception ex) {
            System.out.print("抽取门楼牌数据出错，在插入数据的时候\n");
            System.out.print(ex.toString() + "\n");
            // 获取堆栈跟踪元素
            StackTraceElement[] stackTrace = ex.getStackTrace();
            // 打印异常的位置信息
            for (StackTraceElement element : stackTrace) {
                System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
            }
        }
    }


    //循环  房屋  鼎城
    public void insertFlooorDatadc() {
        String insertField = "guid,zddm,hh,zl,zrzh,bdcdyh,zt,zrzbsm";
        List<String> countLst=psourcetopMapper.getTableCount("v_cds_fwsx");//源数据库
        if(countLst.isEmpty()) return;
        Integer count=Integer.parseInt(countLst.get(0));//总数
        int sum=1;
        int pageSize=10000;
        while (sum<count){
            List<String> values = new ArrayList<>();
            List<Map<String, Object>> lstData=psourcetopMapper.getFloorDataPage(Integer.toString(sum),Integer.toString((sum+pageSize-1)));
            for (Map<String, Object> row : lstData) {
                try {
                    String str = "(";
                    String guidValue = row.get("UUID").toString();//唯一标识
                    for (String field : insertField.split(",")) { //名称
                        String fieldValue = "";
                        switch (field) {
                            case "guid":
                                fieldValue = guidValue;
                                break;
                            case "zddm":
                                fieldValue = row.get("ZDDM").toString();
                                break;
                            case "zl":
                                fieldValue = row.get("ZL") != null ? row.get("ZL").toString() : "";
                                break;
                            case "hh":
                                fieldValue = row.get("HH") != null ? row.get("HH").toString() : "";
                                break;
                            case "zrzh":
                                fieldValue = row.get("ZRZH") != null ? row.get("ZRZH").toString() : "";
                                break;
                            case "bdcdyh":
                                fieldValue = row.get("BDCDYH") != null ? row.get("BDCDYH").toString() : "";
                                break;
                            case "zt":
                                fieldValue = row.get("ZT") != null ? row.get("ZT").toString() : "";
                                break;
                            case "zrzbsm":
                                fieldValue = row.get("ZRZBSM") != null ? row.get("ZRZBSM").toString() : "";
                                break;
                        }
                        str = str + "'" + fieldValue + "',";
                    }
                    str = str.substring(0, str.length() - 1);
                    str = str + ")";
                    values.add(str);

                } catch (Exception ex) {
                    System.out.print("抽取房屋数据出错，在处理房屋数据的时候\n");
                    System.out.print(row);
                    System.out.print(ex.toString() + "\n");
                    // 获取堆栈跟踪元素
                    StackTraceElement[] stackTrace = ex.getStackTrace();
                    // 打印异常的位置信息
                    for (StackTraceElement element : stackTrace) {
                        System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                    }
                }
            }
            try {
                if(values.isEmpty()) return;
                String strValues = "";
                for (String value : values) {
                    strValues = strValues + value + ",";
                }
                strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                pdcidmsMapper.insertData(floortable, insertField, strValues);
                System.out.print("房屋数据抽取"+sum+"\n");
            } catch (Exception ex) {
                System.out.print("抽取房屋数据出错，在插入数据的时候\n");
            }
            finally {

            }
            sum+=pageSize;
        }

    }



    //房屋作为户牌数据插入   鼎城
    public  void floormlpInsertdc(){
        String insertField = "clasid,name,pyname,abbrname,addname,guid,state,zipcode,x,y,sources,bsm,handle,dh,zddm,coldate";
        String nameField = "XQMC";//名称字段
        String BSMField = "BSM";
        String zlField = "ZL";
        List<String> countLst=psourcetopMapper.getTableCount("v_cds_fwsx");//查询有多少数据
        if(countLst.isEmpty()) return;
        int count=Integer.parseInt(countLst.get(0));//总数
        int index=0;
        int sum=1;
        int pageSize=1000;
        while (sum<count){
            List<String> guidList = new ArrayList<>();//guid去重
            List<String> barValues = new ArrayList<>();//门牌值
            List<Map<String, Object>> lstData=psourcetopMapper.getFloormlpData(Integer.toString(sum),Integer.toString(sum+pageSize-1));
            for (Map<String, Object> row : lstData) {
                try {
                    String str = "(";
                    String name = row.get(nameField) != null ? row.get(nameField).toString() : "";
                    String zlValue = row.get(zlField) != null ? row.get(zlField).toString() : "";
                    //判断宗地编码判断是否已经抽取过了,抽取过了不在抽取
                    //两个表都没有的情况下
                    String guidValue = row.get("UUID").toString();
                    if (!guidList.contains(guidValue)) { //目标表、已经插入的都不包含进去
                        guidList.add(guidValue);//后重复
                        String bsmValue=row.get("BSM").toString();
                        for (String field : insertField.split(",")) { //名称
                            String fieldValue = "";
                            switch (field) {
                                case "name":  //地址、简称
                                    String dh=row.get("DH")!=null?row.get("DH").toString():"";
                                    String hh=row.get("HH")!=null?row.get("HH").toString():"";
                                    fieldValue =name+dh+hh;
                                    break;
                                case "abbrname":
                                    fieldValue = name;
                                    break;
                                case "addname":  //坐落 -》标准地址
                                    fieldValue = zlValue;
                                    break;
                                case "x":  //经
                                    fieldValue = row.get("XZBZ").toString().split(",")[0];
                                    break;
                                case "y":  //纬度
                                    fieldValue = row.get("YZBZ").toString().split(",")[0];
                                    break;
                                case "state":  //章台
                                    fieldValue = "在用";
                                    break;
                                case "guid":  //uuid
                                    fieldValue = guidValue;
                                    break;
                                case "zipcode":  //邮编
                                    fieldValue = "415900";
                                    break;
                                case "sources":
                                    fieldValue = "鼎城不动产登记中心";
                                    break;
                                case "coldate":
                                    fieldValue = LocalDate.now().toString();
                                    break;
                                case "pyname":
                                    fieldValue = new PinyinUtil().getFullPinyin(name);
                                    break;
                                case "bsm":
                                    fieldValue = bsmValue;//
                                    break;
                                case "handle":
                                    fieldValue = "0";//表示没有没有编码过，从对方数据库里面获取的

                                    break;
                                case "dh":
                                    fieldValue = row.get("DH") != null ? row.get("DH").toString() : "";
                                    break;
                                case "zddm":
                                    fieldValue = row.get("ZDDM")!=null? row.get("ZDDM").toString():"";
                                    break;
                                case "clasid":
                                    fieldValue = "2930";//户牌
                                    break;
                            }
                            str = str + "'" + fieldValue + "',";
                        }
                        str = str.substring(0, str.length() - 1);
                        str += ")";
                        barValues.add(str);
                    }

                } catch (Exception ex) {
                    System.out.print("抽取门楼拍数据出错，在处理房屋数据的时候\n");
                    System.out.print(row);
                    System.out.print(ex.toString() + "\n");
                    // 获取堆栈跟踪元素
                    StackTraceElement[] stackTrace = ex.getStackTrace();
                    // 打印异常的位置信息
                    for (StackTraceElement element : stackTrace) {
                        System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                    }
                }
            }
            if(!barValues.isEmpty()){
                try {
                    String strValues = "";
                    for (String value : barValues) {
                        strValues = strValues + value + ",";
                    }
                    strValues = strValues.substring(0, strValues.length() - 1);//去除逗号
                    pdcidmsMapper.insertData(mlptable, insertField, strValues);
                    System.out.print("门楼牌数据抽取完成" + sum + "\n");
                }catch (Exception ex){
                    System.out.print("抽取门楼牌数据出错，在插入房屋数据的时候\n");
                    System.out.print(ex.toString() + "\n");
                    // 获取堆栈跟踪元素
                    StackTraceElement[] stackTrace = ex.getStackTrace();
                    // 打印异常的位置信息
                    for (StackTraceElement element : stackTrace) {
                        System.out.println(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
                    }
                }finally {
                    barValues.clear();

                }
            }
            sum+=pageSize;
        }
    }



}