package com.yulin.common.page;

import android.os.Bundle;
import android.view.KeyEvent;

/**
 * Page上下文接口类
 * 包含了和@Page 相关的所有事件
 * emoney
 * @version 1.0
 *
 */
public interface PageContext {
	public PageIntent getPageIntent();
	public void dispatchPageResume(boolean b);
	public void dispatchPageCreated();
	public void dispatchPagePause(boolean b);
	public void dispatchPageDestroy();
	public void dispatchPageResult(int requestCode, int resultCode, Bundle data);
	public void dispatchNewIntent(PageIntent intent);
//	public boolean isAdded();
//	public boolean getUserVisibleHint();
	public void startPage(int containerId, PageIntent intent);
	public void startPage(PageIntent intent);
	public void startPageForResult(int containerId, PageIntent intent, int requestCode);
	public void startPageForResult(PageIntent intent, int requestCode);
	
	public boolean needPauseAndResumeWhenSwitch();//当界面切换的时候是否需要执行状态暂停和恢复的切换
	
	public boolean onKeyDown(int keyCode, KeyEvent event);
	public boolean onKeyUp(int keyCode, KeyEvent event);
	
	public boolean isAdded2Stack();
}
