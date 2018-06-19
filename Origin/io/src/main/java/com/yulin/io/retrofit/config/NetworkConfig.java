package com.yulin.io.retrofit.config;

import com.houchen.io.retrofit.sign.SignCalculate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liu_lei on 2017/6/22.
 */

public class NetworkConfig {

    private int connectTimeout = 10;
    private int writeTimeout = 10;
    private int readTimeout = 10;
    private Map<String,Object> commParameter = new HashMap<>();
    private SignCalculate signCalculate;
    private String baseUrl;

    private NetworkConfig(Builder builder) {
        connectTimeout = builder.connectTimeout;
        writeTimeout = builder.writeTimeout;
        readTimeout = builder.readTimeout;
        commParameter = builder.commParameter;
        signCalculate = builder.signCalculate;
        baseUrl = builder.baseUrl;
    }

    public SignCalculate getSignCalculate() {
        return signCalculate;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public Map<String, Object> getCommParameter() {
        return commParameter;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static Builder Builder() {
        return new Builder();
    }

    public static final class Builder {
        int connectTimeout = 10;
        int writeTimeout = 10;
        int readTimeout = 10;
        Map<String,Object> commParameter = new HashMap<String,Object>();
        SignCalculate signCalculate;
        String baseUrl;

        public Builder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setWriteTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setCommParameter(Map<String, Object> commParameter) {
            this.commParameter = commParameter;
            return this;
        }

        public Builder setSignCalculate(SignCalculate signCalculate) {
            this.signCalculate = signCalculate;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NetworkConfig build() {
            return new NetworkConfig(this);
        }
    }

}
