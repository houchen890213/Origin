package com.yulin.io.retrofit.response;

/**
 * Created by liu_lei on 2017/6/22.
 */

public class DefResponse<T> {

    public static final int SUCCESS_CODE = 0;

    private  Integer retCode;
    private  String  retMsg;
    private  T data;

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public DefResponse setRetMsg(String retMsg) {
        this.retMsg = retMsg;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
