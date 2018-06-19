package com.yulin.common.bar;

import java.util.ArrayList;
import java.util.List;

/**
 * Bar控件的操作菜单 管理一系列菜单项的集合，应用于@TitleBar,@ToolBar,@NavigateBar 等
 * 
 * emoney
 *
 */
public class BarMenu {

	private List<BarMenuItem> mLstItems = new ArrayList<BarMenuItem>();

	/**
	 * 添加菜单项
	 * 
	 * @param item
	 * @return
	 */
	public BarMenuItem addItem(BarMenuItem item) {
		mLstItems.add(item);
		return item;
	}

	/**
	 * 添加多个菜单项
	 * 
	 * @param items
	 */
	public void addItems(List<BarMenuItem> items) {
		mLstItems.addAll(items);
	}

	/**
	 * 获取所有菜单项
	 * 
	 * @return
	 */
	public List<BarMenuItem> getItems() {
		return mLstItems;
	}

	/**
	 * 获取指定位置的菜单项
	 * 
	 * @param index
	 *            菜单项位置
	 * @return
	 */
	public BarMenuItem getItem(int index) {
		return mLstItems.get(index);
	}
	
	/**
     * 设置指定位置的菜单项
     * 
     * @param index
     *            菜单项位置
     * @return
     */
    public void setItem(int index, BarMenuItem item) {
        if (mLstItems.size() > index) {
            mLstItems.set(index, item);
        }
    }





	/**
	 * 清除所有菜单项
	 */
	public void clear() {
		mLstItems.clear();
	}
}
