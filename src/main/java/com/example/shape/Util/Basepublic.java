package com.example.shape.Util;

public class Basepublic<T> {
    private boolean success;
    private T data;
    private String message;
    private String code;

    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public Basepublic(boolean success, T data, ResponseCode responseCode) {
        this.success = success;
        this.data = data;
        this.message = responseCode.getCode();
        this.code = responseCode.getMsg();
    }


    public Basepublic(String code, T data, String error) {
        this.code = code;
        this.data = data;
        this.error = error;
    }


    public Basepublic(String code, String message) {
        this.code = code;
        this.message = message;
        this.success = false;

    }

    public Basepublic(String message) {
        this.code = "";
        this.message = message;
        this.success = false;

    }


    public Basepublic() {
    }

    /***
     * 查询成功
     * @param data
     * @return
     */
    public static Basepublic QueryReturnedSuccessfully(Object data) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.SUCCESS_SELECT.getCode());
        basepublic.setMessage(ResponseCode.SUCCESS_SELECT.getMsg());
        basepublic.setSuccess(true);
        basepublic.setData(data);
        return basepublic;
    }


    /***
     * 查询成功
     * @param data
     * @return
     */
    public static Basepublic QueryReturnedSuccessfullyMsg(Object data, String msg) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.SUCCESS_SELECT.getCode());
        basepublic.setMessage(msg);
        basepublic.setSuccess(true);
        basepublic.setData(data);
        return basepublic;
    }


    /***
     * 查询失败
     * @return
     */
    public static Basepublic QueryReturnFailed(Object data) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.ERROR_SELECT.getCode());
        basepublic.setMessage(ResponseCode.ERROR_SELECT.getMsg());
        basepublic.setSuccess(false);
        return basepublic;
    }

    /***
     * 查询失败
     * @return
     */
    public static Basepublic UpdateReturnFailedBadRequestMsg(String msg) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.BAD_REQUEST.getCode());
        if (msg != null) {
            basepublic.setMessage(msg);
        } else {
            basepublic.setMessage(ResponseCode.BAD_REQUEST.getMsg());
        }
        basepublic.setSuccess(false);
        return basepublic;
    }

    /***
     * 查询失败msg
     * @return
     */
    public static Basepublic QueryReturnFailedMsg(String Msg) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.ERROR_SELECT.getCode());
        basepublic.setMessage(Msg);
        basepublic.setSuccess(false);
        return basepublic;
    }


    /****
     * 操作成功
     * @return
     */
    public static Basepublic ReturnOperationSuccessful() {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.SUCCESS_OPTION.getCode());
        basepublic.setMessage(ResponseCode.SUCCESS_OPTION.getMsg());
        basepublic.setSuccess(true);
        return basepublic;
    }


    /***
     * 操作成功
     * @param data
     * @return
     */
    public static Basepublic ReturnOperationSuccessfulData(Object data, String msg) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.SUCCESS_OPTION.getCode());
        basepublic.setMessage(msg);
        basepublic.setSuccess(true);
        basepublic.setData(data);
        return basepublic;
    }


    /****
     * 操作成功有返回值
     * @return
     */
    public static Basepublic ReturnOperationSuccessfulSetMsg(String msg) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.SUCCESS_OPTION.getCode());
//        basepublic.setMessage(ResponseCode.SUCCESS_OPTION.getMsg());
        basepublic.setMessage(msg);
        basepublic.setSuccess(true);
        return basepublic;
    }

    /****
     * 操作失败有返回值
     * @return
     */
    public static Basepublic ReturnOperationFailedSetData(Object data) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.ERROR_OPTION.getCode());
        basepublic.setMessage(ResponseCode.ERROR_OPTION.getMsg());
        basepublic.setData(data);
        basepublic.setSuccess(false);
        return basepublic;
    }

    /****
     * 操作失败有返回值
     * @return
     */
    public static Basepublic ReturnOperationFailedSetDataMsg(Object data, String msg) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.ERROR_OPTION.getCode());
        basepublic.setMessage(msg);
        basepublic.setData(data);
        basepublic.setSuccess(false);
        return basepublic;
    }


    /****
     * 操作失败
     * @return
     */
    public static Basepublic ReturnOperationFailed(String msg) {
        Basepublic basepublic = new Basepublic();
        basepublic.setCode(ResponseCode.ERROR_OPTION.getCode());
        basepublic.setMessage(msg);
        basepublic.setSuccess(false);
        return basepublic;
    }


    /**
     * 操作失败，并自定义data和msg
     *
     * @param data
     * @param msg
     * @return
     */
    public static Basepublic<Object> ReturnOperationFailedSetDataAndMsg(Object data, String msg) {
        Basepublic<Object> basePublic = new Basepublic();
        basePublic.setCode(ResponseCode.ERROR_OPTION.getCode());
        basePublic.setMessage(msg);
        basePublic.setData(data);
        basePublic.setSuccess(false);
        return basePublic;
    }

    /**
     * 操作失败，并自定义data和msg
     *
     * @param data
     * @param msg
     * @return
     */
    public static <T> Basepublic<T> fail(T data, String msg) {
        Basepublic<T> basePublic = new Basepublic();
        basePublic.setCode(ResponseCode.ERROR_OPTION.getCode());
        basePublic.setMessage(msg);
        basePublic.setData(data);
        basePublic.setSuccess(false);
        return basePublic;
    }
    /**
     * 操作失败，并自定义data和msg
     *
     * @param data
     * @param msg
     * @return
     */
    public static <T> Basepublic<T> ok(T data, String msg) {
        Basepublic<T> basePublic = new Basepublic();
        basePublic.setCode(ResponseCode.SUCCESS_OPTION.getCode());
        basePublic.setMessage(msg);
        basePublic.setData(data);
        basePublic.setSuccess(false);
        return basePublic;
    }

}
