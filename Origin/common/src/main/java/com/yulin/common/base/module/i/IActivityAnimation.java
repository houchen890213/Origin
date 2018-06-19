package com.yulin.common.base.module.i;

/**
 * Created by liu_lei on 2017/10/11.
 *
 * activity切换动画
 */

public interface IActivityAnimation {

    /**
     * 进入动画
     */
    int enterAnimation();

    /**
     * 退出动画
     */
    int exitAnimation();

    /**
     * 是否支持动画
     */
    boolean isSupportAnimation();

}
