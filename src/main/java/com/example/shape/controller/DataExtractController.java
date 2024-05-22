package com.example.shape.controller;


import com.example.shape.Service.dataExtract.DataExtractService;
import com.example.shape.Util.Basepublic;
import com.example.shape.Util.encrypt.WcspEncrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(tags = "数据抽取", value = "数据抽取")
@RequestMapping("/api")
public class DataExtractController {

    @Autowired
    DataExtractService dataExtractService;
    @ApiOperation(value = "1、抽取 \n 0是房屋户牌，1、房屋 2是小区 3、是宗地  4是门楼盘 5是房屋户牌鼎城，6、房屋鼎城 7是小区鼎城 8、是宗地鼎城  9是 门楼牌鼎城")
    @RequestMapping(value = "/dataExtract", method = RequestMethod.GET)
    public @ResponseBody Basepublic entryData(String type) {
        boolean result=dataExtractService.dataExtract(type);
        return result?Basepublic.ReturnOperationSuccessful():Basepublic.ReturnOperationFailedSetData(null);
    }
    @ApiOperation(value = "2、测试sql")
    @RequestMapping(value = "/getSource", method = RequestMethod.GET)
    public List<Map<String, Object>> getSource(String sql) {
        //测试sql
         return dataExtractService.getSource(sql);
    }
    @ApiOperation(value = "3、测试sql2")
    @RequestMapping(value = "/getSource1", method = RequestMethod.GET)
    public List<Map<String, Object>> getSource1(String sql) {
        //测试sql
        return dataExtractService.getSource1(sql);
    }
}
