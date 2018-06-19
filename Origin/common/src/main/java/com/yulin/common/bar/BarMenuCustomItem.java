package com.yulin.common.bar;

import android.view.View;

/**
 * 自定义view的菜单项
 */
public class BarMenuCustomItem extends BarMenuItem {

	private View mCustomView = null;
	public BarMenuCustomItem(int itemId) {
		mItemId = itemId;
	}
	public BarMenuCustomItem(int itemId, View view) {
		mItemId = itemId;
		mCustomView = view;
	}

	public void setCustomView(View view)
	{
		mCustomView = view;
	}
	
	public View getCustomView()
	{
		return mCustomView;
	}
}
