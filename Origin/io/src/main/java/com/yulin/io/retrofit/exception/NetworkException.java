package com.yulin.io.retrofit.exception;

/**
 * Created by liu_lei on 2017/6/22.
 */

public class NetworkException extends Exception {

    public static final int INITCONFIG_ERROR = 1000;
    public static final int TOKEN_ERROR = INITCONFIG_ERROR + 1;
    public static final int NOTNETWORK_ERROR = TOKEN_ERROR + 1;
    public static final int NETWORK_ERROR = NOTNETWORK_ERROR + 1;
    public static final int SYSTEM_ERROR = NETWORK_ERROR + 1;
    public static final int JSON_ERROR = SYSTEM_ERROR + 1;
    public static final int HINT_ERROR = JSON_ERROR + 1;
    public static final int CREATE_UNION_ERROR = HINT_ERROR + 1;
    public static final int SERVER_ERROR = -1;

    private int errorCode;
    private static boolean Debug = false;

    public NetworkException(int errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public NetworkException(int errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
