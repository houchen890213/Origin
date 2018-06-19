package com.yulin.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by liu_lei on 2017/5/29.
 *
 * 封装activity 跳转
 */

public class ActivityUtil {

    public static void next(Context context, Intent intent) {
        if (context != null && intent != null)
            context.startActivity(intent);
    }

    public static void next(Context context, Class<? extends Activity> clz) {
        Intent intent = new Intent(context, clz);
        next(context, intent);
    }

}
