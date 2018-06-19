package com.yulin.common.utils;

import android.text.TextUtils;

/**
 * Created by liu_lei on 2017/6/29.
 */

public class StringUtil {

    public static String removePrefix0(String value) {
        if (!TextUtils.isEmpty(value)) {
            while (value.startsWith("0")) {
                value = value.substring(1);
            }
        }

        return value;
    }

}
