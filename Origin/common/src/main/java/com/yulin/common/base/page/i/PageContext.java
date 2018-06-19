package com.yulin.common.base.page.i;

import android.os.Bundle;
import android.view.KeyEvent;

import com.yulin.common.base.page.PageIntent;

/**
 * Page上下文接口类
 * 包含了和@Page 相关的所有事件
 */

public interface PageContext {
	
	PageIntent getPageIntent();

	void dispatchPageCreated();
	void dispatchPageResume(boolean b);
	void dispatchPagePause(boolean b);
	void dispatchPageDestroy();

	void dispatchNewIntent(PageIntent intent);
	void dispatchPageResult(int requestCode, int resultCode, Bundle data);

	void startPage(int containerId, PageIntent intent);
	void startPage(PageIntent intent);
	void startPageForResult(int containerId, PageIntent intent, int requestCode);
	void startPageForResult(PageIntent intent, int requestCode);
	
	boolean onKeyDown(int keyCode, KeyEvent event);
	boolean onKeyUp(int keyCode, KeyEvent event);

	boolean needPauseAndResumeWhenSwitch();//当界面切换的时候是否需要执行状态暂停和恢复的切换
	boolean isAdded2Stack();

}
