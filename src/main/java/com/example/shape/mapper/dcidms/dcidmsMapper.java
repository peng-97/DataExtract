package com.example.shape.mapper.dcidms;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface dcidmsMapper {
     public List<Map<String,Object>> selectRes(@Param("tableName")String tableName);

     public  void insertData(@Param("tableName")String tableName,@Param("columns")String columns,@Param("values")String values);

     public  void  deleteFloorData();

     public List<Map<String,Object>> getsource(@Param("sql")String sql);
}