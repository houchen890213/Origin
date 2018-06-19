package com.yulin.common.page.i;

import com.yulin.common.bar.Bar;
import com.yulin.common.bar.BarMenu;
import com.yulin.common.bar.BarMenuItem;

/**
 * Bar控件的创建与事件触发接口
 * 通过bindBar绑定到Page中
 * 一般多用于@TitleBar
 * 
 * emoney
 * @version 1.0
 *
 */
public interface PageBarI {
	/**
	 * 创建bar菜单
	 * @param barId
	 * @param barMenu 菜单
	 * @return
	 */
	public boolean onCreatePageTitleBarMenu(Bar bar, BarMenu barMenu);
	
	/**
	 * bar菜单项点击事件
	 * @param barId
	 * @param menuItem 菜单项
	 */
	public void onPageTitleBarMenuItemSelected(BarMenuItem menuItem);
}
