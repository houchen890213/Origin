package com.yulin.common.adapter;

import android.databinding.BaseObservable;

/**
 * Created by liu lei on 2017/5/12.
 *
 * 类中用于保存viewType
 * 多种item的列表，数据项必须继承这个类，构造函数强制子类上传viewType。
 */

public class MultiItemBean extends BaseObservable {

    private int viewType;

    protected MultiItemBean(int viewType) {
        this.viewType = viewType;
    }

    public int getItemViewType() {
        return viewType;
    }

}
