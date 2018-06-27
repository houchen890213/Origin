package com.yulin.origin.network;

import android.content.Context;

import com.yulin.common.utils.DeviceUtil;
import com.yulin.io.retrofit.config.NetworkConfig;
import com.yulin.io.retrofit.invocation.RetrofitServiceProxy;
import com.yulin.io.retrofit.manager.ServiceManager;
import com.yulin.io.retrofit.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liu_lei on 2017/5/28.
 *
 *  初始化网络框架，生成OkHttpClient对象及retrofit。
 *  创建service对象。
 */

public class NetworkManager extends ServiceManager {

    private static NetworkManager instance;

    private NetworkManager() {
        super(new RetrofitServiceProxy());
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }

        return instance;
    }

    public void init(File cacheDir) {
        final Map<String, Object> map = new HashMap<>();
        map.put("channel", "1");

        final NetworkConfig networkConfig = NetworkConfig.Builder()
                .setBaseUrl(NetConstant.HOST)
//                .setCommParameter(map)
//                .setSignCalculate(new DefSignCalculateImpl("7FAF443E9BF54DB98EA89B9F7FBA30AB"))
                .build();

//        OkHttpContext.getInstance().doInit(cacheDir, networkConfig);
    }

    public void init(Context context) {
        final Map<String, Object> map = new HashMap<>();
        map.put("version", DeviceUtil.getAppVersionName(context));
        map.put("deviceId", DeviceUtil.getPrivateDeviceID(context));

        final NetworkConfig networkAPIConfig = NetworkConfig.Builder()
                .setBaseUrl(NetConstant.HOST)
                .setCommParameter(map)
                .build();

        OkHttpUtils.getInstance().initConfig(networkAPIConfig);
    }

}
