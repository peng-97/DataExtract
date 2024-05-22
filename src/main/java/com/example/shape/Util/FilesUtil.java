package com.example.shape.Util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @package com.example.shape.Util
 * @project CityBrainProject
 * @description [类型描述]
 * @createTime 2023/11/2 17:32
 * @author>zsq
 */
public class FilesUtil {

    public static LinkedList<Map> setingFile(){
        LinkedList metadata = new LinkedList();
        Map metadataMap = new HashMap();
        LinkedList list = new LinkedList();
        Map map1 = new HashMap();
        map1.put("fieldkey","dataIndex");
        map1.put("fieldname","排名");
        map1.put("unit",null);
        Map map2 = new HashMap();
        map2.put("fieldkey","sbyx_text");
        map2.put("fieldname","银行名称");
        map2.put("unit",null);
        Map map3 = new HashMap();
        map3.put("fieldkey","projectnum");
        map3.put("fieldname","项目数");
        map3.put("unit",null);
        Map map4 = new HashMap();
        map4.put("fieldkey","htje");
        map4.put("fieldname","合同总金额（亿元）");
        map4.put("unit",null);
        Map map5 = new HashMap();
        map5.put("fieldkey","tyzje");
        map5.put("fieldname","提用总金额（亿元）");
        map5.put("unit",null);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        metadataMap.put("fields",list);
        metadata.add(metadataMap);
        return metadata;
    }



    public static LinkedList<Map> setingFile2(){
        LinkedList metadata = new LinkedList();
        Map metadataMap = new HashMap();
        LinkedList list = new LinkedList();
        Map map1 = new HashMap();
        map1.put("fieldkey","dataIndex");
        map1.put("fieldname","排名");
        map1.put("unit",null);
        Map map2 = new HashMap();
        map2.put("fieldkey","streetname_text");
        map2.put("fieldname","街道名称");
        map2.put("unit",null);
        Map map3 = new HashMap();
        map3.put("fieldkey","projectnum");
        map3.put("fieldname","项目数");
        map3.put("unit",null);
        Map map4 = new HashMap();
        map4.put("fieldkey","htje");
        map4.put("fieldname","合同总金额（亿元）");
        map4.put("unit",null);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        metadataMap.put("fields",list);
        metadata.add(metadataMap);
        return metadata;
    }

}
