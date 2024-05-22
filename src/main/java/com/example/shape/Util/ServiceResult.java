package com.example.shape.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@JSONType(
        orders = {"code", "message", "pageNum", "pageSize", "total", "count", "metadata", "summary", "style", "data"}
)
public class ServiceResult implements Serializable {
    private Integer code = 0;
    private String message = "";
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Long count;
    private List<?> metadata;
    private List<?> summary;
    private Map<String, ?> style;
    private List<?> data;

    public ServiceResult() {
    }

    public ServiceResult(List<?> data) {
        this.data = data;
        if (data != null) {
            this.count = (long)data.size();
        }

    }

    public ServiceResult(PageInfo<?> pageInfo) {
        this.data = pageInfo.getList();
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.count = pageInfo.getTotal();
    }

    public ServiceResult(List<?> metadata, List<?> data) {
        this.metadata = metadata;
        this.data = data;
        if (data != null) {
            this.count = (long)data.size();
        }

    }

    public ServiceResult(Map<String, ?> style, List<?> data) {
        this.style = style;
        this.data = data;
        if (data != null) {
            this.count = (long)data.size();
        }

    }

    public ServiceResult(List<?> metadata, Map<String, ?> style, List<?> data) {
        this.metadata = metadata;
        this.style = style;
        this.data = data;
        if (data != null) {
            this.count = (long)data.size();
        }

    }

    public ServiceResult(List<?> metadata, PageInfo<?> pageInfo) {
        this.metadata = metadata;
        this.data = pageInfo.getList();
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.count = pageInfo.getTotal();
    }

    public ServiceResult(String message, List<?> data) {
        this.message = message;
        this.data = data;
    }

    public ServiceResult(List<?> data, Long total) {
        this.total = total;
        this.data = data;
        if (data != null) {
            this.count = (long)data.size();
        }

    }

    public ServiceResult(ServiceResult.ServiceErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ServiceResult(ServiceResult.ServiceErrorCode errorCode, String errorDetails) {
        this.code = errorCode.getCode();
        if (errorDetails != null) {
            if (errorCode.getMessage() != null && !errorCode.getMessage().isEmpty()) {
                this.message = errorCode.getMessage() + "：" + errorDetails;
            } else {
                this.message = errorDetails;
            }
        }

    }

    public ServiceResult(ServiceResult.ServiceErrorCode errorCode, String errorDetails, List<?> data) {
        this.code = errorCode.getCode();
        if (errorDetails != null) {
            if (errorCode.getMessage() != null && !errorCode.getMessage().isEmpty()) {
                this.message = errorCode.getMessage() + "：" + errorDetails;
            } else {
                this.message = errorDetails;
            }
        }

        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPageNum() {
        return this.pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum=pageNum;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize=pageSize;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCount() {
        return this.count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<?> getMetadata() {
        return this.metadata;
    }

    public void setMetadata(List<?> metadata) {
        this.metadata = metadata;
    }

    public List<?> getSummary() {
        return this.summary;
    }

    public void setSummary(List<?> summary) {
        this.summary = summary;
    }

    public Map<String, ?> getStyle() {
        return this.style;
    }

    public void setStyle(Map<String, ?> style) {
        this.style = style;
    }

    public List<?> getData() {
        return this.data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public void setMetadataDisplayname(String displayName) {
        ServiceResult.Metadata mdata = null;
        if (this.metadata != null && this.metadata.size() != 0) {
            if (this.metadata.get(0) instanceof ServiceResult.Metadata) {
                mdata = (ServiceResult.Metadata)this.metadata.get(0);
            }
        } else {
            mdata = new ServiceResult.Metadata();
            this.metadata = Collections.singletonList(mdata);
        }

        if (mdata != null) {
            mdata.setDisplayname(displayName);
        }

    }

    public void setMetadataLastupdate(String lastupdate) {
        ServiceResult.Metadata mdata = null;
        if (this.metadata != null && this.metadata.size() != 0) {
            if (this.metadata.get(0) instanceof ServiceResult.Metadata) {
                mdata = (ServiceResult.Metadata)this.metadata.get(0);
            }
        } else {
            mdata = new ServiceResult.Metadata();
            this.metadata = Collections.singletonList(mdata);
        }

        if (mdata != null) {
            mdata.setLastupdate(lastupdate);
        }

    }

    public void setMetadataDimension(List<String> dimension) {
        if (dimension != null) {
            ServiceResult.Metadata mdata = null;
            if (this.metadata != null && this.metadata.size() != 0) {
                if (this.metadata.get(0) instanceof ServiceResult.Metadata) {
                    mdata = (ServiceResult.Metadata)this.metadata.get(0);
                }
            } else {
                mdata = new ServiceResult.Metadata();
                this.metadata = Collections.singletonList(mdata);
            }

            if (mdata != null) {
                mdata.setDimension(dimension);
            }
        }

    }

    public void addMetadataField(String fieldkey, String fieldname, String unit) {
        ServiceResult.Metadata mdata = null;
        if (this.metadata != null && this.metadata.size() != 0) {
            if (this.metadata.get(0) instanceof ServiceResult.Metadata) {
                mdata = (ServiceResult.Metadata)this.metadata.get(0);
            }
        } else {
            mdata = new ServiceResult.Metadata();
            this.metadata = Collections.singletonList(mdata);
        }

        if (mdata != null) {
            mdata.addField(new ServiceResult.Field(fieldkey, fieldname, unit));
        }

    }

    public void addMetadataField(String fieldkey, String fieldname) {
        this.addMetadataField(fieldkey, fieldname, (String)null);
    }

    public void removeMetadataField(String fieldkey) {
        if (this.metadata != null && this.metadata.size() > 0 && this.metadata.get(0) instanceof ServiceResult.Metadata) {
            ServiceResult.Metadata mdata = (ServiceResult.Metadata)this.metadata.get(0);
            mdata.removeField(fieldkey);
        }

    }

    public void dimensionalize(List<String> dimenProperties) {
        this.dimensionalize(dimenProperties, false);
    }

    public void dimensionalize(List<String> dimenProperties, boolean retainProperty) {
        if (this.data != null) {
            List<Object> newData = new ArrayList();
            Iterator var5 = this.data.iterator();

            while(true) {
                Map newElement;
                do {
                    if (!var5.hasNext()) {
                        if (!retainProperty) {
                            dimenProperties.stream().forEach(this::removeMetadataField);
                        }

                        this.data = newData;
                        this.setMetadataDimension(Arrays.asList("name", "value"));
                        return;
                    }

                    Object element = var5.next();
                    newElement = this.getMapObject(element);
                } while(newElement == null);

                List<Map<String, Object>> children = new ArrayList();
                Iterator var8 = dimenProperties.iterator();

                while(var8.hasNext()) {
                    String property = (String)var8.next();
                    if (property != null && newElement.containsKey(property)) {
                        Map<String, Object> child = new LinkedHashMap();
                        child.put("key", property);
                        child.put("value", newElement.get(property));
                        if (this.metadata != null && this.metadata.size() > 0 && this.metadata.get(0) != null) {
                            Map<String, Object> metaData = this.getMapObject(this.metadata.get(0));
                            if (metaData != null && metaData.containsKey("fields") && metaData.get("fields") instanceof List) {
                                List found = (List)((List)metaData.get("fields")).stream().filter((o) -> {
                                    return o != null && o instanceof Map && ((Map)o).containsKey("fieldkey") && property.equals(((Map)o).get("fieldkey"));
                                }).collect(Collectors.toList());
                                if (found.size() > 0) {
                                    Map field = (Map)found.get(0);
                                    child.put("name", field.get("fieldname"));
                                    if (field.containsKey("unit")) {
                                        child.put("unit", field.get("unit"));
                                    }
                                }
                            }
                        }

                        children.add(child);
                        if (!retainProperty) {
                            newElement.remove(property);
                        }
                    }
                }

                newElement.put("children", children);
                newData.add(newElement);
            }
        }
    }

    private Map<String, Object> getMapObject(Object obj) {
        return obj instanceof Map ? (Map)obj : (Map) JSONObject.parse(JSONObject.toJSONStringWithDateFormat(obj, JSON.DEFFAULT_DATE_FORMAT, new SerializerFeature[0]));
    }

    @JSONType(
            orders = {"displayname", "lastupdate", "fields", "dimension"}
    )
    private class Metadata implements Serializable {
        private String displayname;
        private String lastupdate;
        private List<ServiceResult.Field> fields;
        private List<String> dimension;

        private Metadata() {
        }

        public String getDisplayname() {
            return this.displayname;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public String getLastupdate() {
            return this.lastupdate;
        }

        public void setLastupdate(String lastupdate) {
            this.lastupdate = lastupdate;
        }

        public synchronized void addField(ServiceResult.Field field) {
            if (field != null) {
                if (this.fields == null) {
                    this.fields = new ArrayList();
                }

                this.fields.add(field);
            }
        }

        public synchronized void removeField(ServiceResult.Field field) {
            if (this.fields != null && field != null) {
                this.fields.remove(field);
                if (this.fields.size() == 0) {
                    this.fields = null;
                }

            }
        }

        public void removeField(String fieldkey) {
            this.removeField(ServiceResult.this.new Field(fieldkey, (String)null));
        }

        public List<ServiceResult.Field> getFields() {
            return this.fields;
        }

        public List<String> getDimension() {
            return this.dimension;
        }

        public void setDimension(List<String> dimension) {
            this.dimension = dimension;
        }
    }

    private class Field implements Serializable {
        private String fieldkey;
        private String fieldname;
        private String unit;

        public Field(String fieldkey, String fieldname, String unit) {
            this.fieldkey = fieldkey;
            this.fieldname = fieldname;
            this.unit = unit;
        }

        public Field(String fieldkey, String fieldname) {
            this.fieldkey = fieldkey;
            this.fieldname = fieldname;
        }

        public String getFieldkey() {
            return this.fieldkey;
        }

        public void setFieldkey(String fieldkey) {
            this.fieldkey = fieldkey;
        }

        public String getFieldname() {
            return this.fieldname;
        }

        public void setFieldname(String fieldname) {
            this.fieldname = fieldname;
        }

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public boolean equals(Object obj) {
            return obj instanceof ServiceResult.Field && (this.fieldkey == null && ((ServiceResult.Field)obj).getFieldkey() == null || this.fieldkey != null && this.fieldkey.equals(((ServiceResult.Field)obj).getFieldkey()));
        }
    }

    public static enum ServiceErrorCode {
        SERVICE_SUCCESS(0, (String)null),
        UNKNOWN_ERROR(1000, "未知异常"),
        NETWORK_ERROR(1001, "网络异常"),
        DATABASE_ERROR(1002, "数据库异常"),
        INVALID_METHOD(1003, "无效方法名"),
        INVALID_PARAMETER(1004, "无效参数名"),
        INVALID_TIMEDATE(1005, "无效时间类型"),
        DATA_ERROR(1006, "无效传入数据"),
        INTERNAL_ERROR(1007, "内部错误"),
        WARN_INVALID(1008, "警告操作无效");

        private int code;
        private String message;

        private ServiceErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
