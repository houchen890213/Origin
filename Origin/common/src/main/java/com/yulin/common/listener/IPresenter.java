package com.yulin.common.listener;

import android.view.View;

/**
 * Created by liu_lei on 2017/5/31.
 * <p>
 * data_binding中点击事件，由该接口中方法处理。
 * 具体页面实现这个接口，并实现相关方法。
 */

public interface IPresenter {

    void onViewClick(View view);

    void onCheckedChanged(View view, boolean isChecked);

}
