package com.yulin.frame.base.ui;

import android.databinding.ViewDataBinding;

import com.yulin.common.base.module.ListModule;
import com.yulin.frame.data.DataModule;
import com.yulin.frame.data.UserInfo;

/**
 * Created by liu_lei on 2017/7/9.
 * 所有activity继承这个类
 * Module类的访问权限被限制在包内，不可在外部继承
 * 所有activity间接继承了RefreshModule，包括有刷新id的类会有下拉刷新功能，没有的不会有下拉刷新功能
 * 该类位置在app模块，包含与业务相关的公用代码
 */

public abstract class ModuleImpl<T extends ViewDataBinding> extends ListModule<T> {

    protected UserInfo getUserInfo() {
        return DataModule.getInstance().getUserInfo();
    }

    /**
     * 是否已登录
     */
    protected boolean isLogin() {
        return getUserInfo().isLogin();
    }

}
