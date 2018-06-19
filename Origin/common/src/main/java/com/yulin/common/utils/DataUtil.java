package com.yulin.common.utils;

import android.text.TextUtils;

/**
 * Created by liu_lei on 2017/6/29.
 */

public class DataUtil {

    public static int convertToInt(String value) {
        if (!TextUtils.isEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

}
