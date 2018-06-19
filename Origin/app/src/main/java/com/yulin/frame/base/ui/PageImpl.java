package com.yulin.frame.base.ui;

import android.databinding.ViewDataBinding;

import com.yulin.common.base.page.ListPage;
import com.yulin.frame.data.DataModule;
import com.yulin.frame.data.UserInfo;

/**
 * Created by liu_lei on 2017/7/9.
 * 所有page继承这个类
 * Page类的访问权限被限制在包内，不可在外部继承
 * 所有activity间接继承了RefreshPage，包括有刷新id的类会有下拉刷新功能，没有的不会有下拉刷新功能
 * 该类位置在app模块，包含与业务相关的公用代码
 */

public class PageImpl<T extends ViewDataBinding> extends ListPage<T> {

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
