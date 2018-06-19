package com.yulin.io.retrofit.response;

/**
 * Created by sky on 2016/7/24 0024.
 *
 */
public class RequestRet {
    public int retCode = 0;
    public String retMsg;
    public int protocolId;
    public Object resData;

    public RequestRet(int retCode) {
        this.retCode = retCode;
    }

    public RequestRet() {
    }
}