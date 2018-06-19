package com.yulin.origin.app;

import android.app.Application;

import com.yulin.origin.network.NetworkManager;

/**
 * Created by liu_lei on 2017/5/28.
 *
 */

public class PoemApplication extends Application {

    private static PoemApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        // 初始化网络请求设置
        NetworkManager.getInstance().init(this);
    }

    public static PoemApplication getInstance() {
        return mInstance;
    }

}
