package com.yulin.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by liu_lei on 2017/6/14.
 *
 * 获取应用相关信息
 * 1. meta_data
 */

public class AppUtil {

    ///////////////////////// 获取meta_data

    /**
     * 获取application标签中的meta_data值
     * */
    public static String getApplicationMetaData(Context context, String name) {
        if (context == null || name == null || name.length() == 0) return "";

        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

}
