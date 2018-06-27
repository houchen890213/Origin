package com.yulin.frame.base.vm;

import android.databinding.BaseObservable;

import com.yulin.frame.data.DataModule;
import com.yulin.frame.data.UserInfo;

/**
 * Created by liu_lei on 2017/5/22.
 * <p>
 * 业务处理逻辑的基类
 */

public class BaseVm extends BaseObservable {

    /**
     * 获取用户个人信息
     */
    protected UserInfo getUserInfo() {
        return DataModule.getInstance().getUserInfo();
    }

}
