package com.yulin.io.retrofit.listener;

/**
 * Created by liu_lei on 2017/6/20.
 * 网络结果回调
 * T为retrofit中的数据T，这种作法下，数据T的解析和处理在ui类中处理
 */

public interface OnApiListener<T> extends OnSuccessListener<T>, OnErrorListener {
}
