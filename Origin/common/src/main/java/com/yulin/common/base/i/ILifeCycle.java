package com.yulin.common.base.i;

/**
 * Created by liu_lei on 2017/10/11.
 *
 * 所有的activity都必须实现的接口
 */

public interface ILifeCycle {

    /**
     * 初始化view
     */
    void initView();

    /**
     * 初始化事件监听
     */
    void initListener();

    /**
     * 初始化数据
     */
    void initData();

}
