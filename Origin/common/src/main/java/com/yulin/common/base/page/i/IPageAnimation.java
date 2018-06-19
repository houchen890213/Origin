package com.yulin.common.base.page.i;


/**
 * Page动画接口，每个page都需要实现
 */
public interface IPageAnimation {

	int enterAnimation();
	int exitAnimation();
	int popEnterAnimation();
	int popExitAnimation();

}
