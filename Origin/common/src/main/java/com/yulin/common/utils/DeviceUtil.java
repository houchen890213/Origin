package com.yulin.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.yulin.common.logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 获取手机设置信息
 * 获取应用信息
 */
public class DeviceUtil {

    private static String deviceId;
    private static String[] cpuInfo;
    private static String subscriberId;
    private static String carrier;
    private static String imei;

    /**
     * 获取设备id
     *
     * @return
     */
    public synchronized static String getPrivateDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public synchronized static String[] getCpuInfo() {
        if (cpuInfo == null) {
            String path = "/proc/cpuinfo";
            try {
                cpuInfo = new String[]{"", ""};
                FileReader fr = new FileReader(path);
                BufferedReader localBufferedReader = new BufferedReader(fr, 4096);
                String line = localBufferedReader.readLine();
                String[] words = line.split("\\s+");
                for (int i = 2; i < words.length; i++) {
                    cpuInfo[0] = cpuInfo[0] + words[i] + " ";
                }
                line = localBufferedReader.readLine();
                words = line.split("\\s+");
                cpuInfo[1] += words[2];
                localBufferedReader.close();
            } catch (IOException ex) {
                Logger.e(ex.getMessage());
            }
        }
        return cpuInfo;
    }

    /**
     * 获取应用版本号
     */
    public static int getAppVersionCode(Context context) {
        PackageInfo pInfo = getPackageInfo(context, getPackageName(context));

        if (pInfo != null)
            return pInfo.versionCode;

        return 0;
    }

    /**
     * 获取应用版本号
     */
    public static String getAppVersionName(Context context) {
        PackageInfo pInfo = getPackageInfo(context, getPackageName(context));

        if (pInfo != null)
            return pInfo.versionName;

        return "";
    }

    /**
     * 获取应用名字
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(context), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    /**
     * 获取应用包信息
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        if (context != null && packageName != null) {
            try {
                return context.getPackageManager().getPackageInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName(Context context) {
        if (context != null)
            return context.getPackageName();

        return "";
    }

    /**
     * 获取display metrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 检查应用是否已安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        return packageInfo != null;
    }

}
