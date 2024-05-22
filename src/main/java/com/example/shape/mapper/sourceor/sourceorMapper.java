package com.example.shape.mapper.sourceor;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface sourceorMapper {
    public List<Map<String,Object>> getDatset(@Param("tableName")String tableName);
    //房屋信息
    public  List<Map<String,Object>> getFloorData();

    public  List<Map<String,Object>> getxqData();

    public  List<Map<String,Object>> getlpData();

    public  List<Map<String,Object>> getfwplData();

    public  List<Map<String,Object>> getzdData();

    public  List<Map<String,String>> testse();

    public   List<Map<String,Object>> getsource(@Param("sql")String sql);

    public  List<Map<String,Object>> getFloorDataPage(@Param("start")String start,@Param("end")String end);


    public  List<String> getTableCount(@Param("tableName")String tableName);
}
