package com.example.shape.Util;

/**
 * Created by ly on 2019/9/20.
 */
public enum ResponseCode {
    SUCCESS_SELECT("200","查询成功"),
    SYSTEM_ERROR("-001", "系统异常"),
    BAD_REQUEST("-002", "错误的请求参数"),
    NOT_FOUND("-003", "找不到请求路径！"),
    CONNECTION_ERROR("-004", "网络连接请求失败！"),
    METHOD_NOT_ALLOWED("-005", "不合法的请求方式"),
    DATABASE_ERROR("-004", "数据库异常"),
    BOUND_STATEMENT_NOT_FOUNT("-006", "找不到方法！"),
    REPEAT_REGISTER("001", "重复注册"),
    NO_USER_EXIST("002", "用户不存在"),
    INVALID_PASSWORD("003", "密码错误"),
    NO_PERMISSION("004", "非法请求！"),
    SUCCESS_OPTION("200", "操作成功！"),
    No_FileSELECT("021", "未选择文件"),
    FILEUPLOAD_SUCCESS("022", "上传成功"),
    NOLOGIN("-10000", "警告！用户未登录，请重新登录！"),
    ILLEGAL_ARGUMENT("024", "参数不合法"),
    ERROR_SELECT("-200", "查询失败"),
    ERROR_OPTION("500", "操作失败"),
    ERROR_USERID("00", "未获取到用户id"),
    ERROR_JOINERROR("300","同步失败"),
    ERROR("","");


    private String code;
    private String msg;

    private ResponseCode(String code, String msg) {

        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;

    }
}