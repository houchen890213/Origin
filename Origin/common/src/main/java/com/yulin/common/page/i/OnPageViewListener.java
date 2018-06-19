package com.yulin.common.page.i;

import android.os.Bundle;
import android.view.KeyEvent;

import com.yulin.common.base.module.Module;
import com.yulin.common.page.Page;

/**
 * 
 * 包含有Page的View视图接口
 * View中包含的所有Page将由父Page管理
 * 
 * emoney
 * @version 1.0
 *
 */
public interface OnPageViewListener {
	/**
	 * 将此接口注册到某个page，则该接口所有page的生命周期将在page销毁后结束
	 * @param page
	 */
	public void registToPage(Page page);
	
	/**
	 * 将此接口注册到某个module，则该接口所有page的生命周期将在module销毁后结束
	 * @param EMActivity
	 */
	public void registToAct(Module EMActivity);
	
	/**
	 * 此接口对应的view视图被销毁
	 */
	public void onViewDestroy();
	
	/**
	 * view视图状态恢复
	 */
	public void onViewResume();
	
	/**
	 * view视图暂停
	 */
	public void onViewPause();
	
	/**
	 * 按键按下事件
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event);
	
	/**
	 * 按键松开事件
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event);
	
	public void onViewResult(int requestCode, int resultCode, Bundle data);
}
