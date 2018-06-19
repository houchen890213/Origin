package com.yulin.common.bar;

import android.view.View;

/**
 * 基础菜单项
 * 用于Bar控件
 */
public abstract class BarMenuItem {

	protected int mItemId = 0;
	protected Object mItemTag = null;
	protected int mItemBackgroundResource = 0;
	protected int mItemBackgroundColor = 0;
	protected int mItemVisibility = View.VISIBLE;
	protected View mItemView = null;
	public int getItemId()
	{
		return mItemId;
	}
	
	public void setItemBackgroundResource(int resId)
	{
		mItemBackgroundResource = resId;
	}
	
	public int getItemBackgroundResource()
	{
		return mItemBackgroundResource;
	}
	
	public void setItemBackgroundColor(int color)
	{
		mItemBackgroundColor = color;
	}
	
	public int getItemBackgroundColor()
	{
		return mItemBackgroundColor;
	}
	
	public void setTag(Object tag)
	{
		mItemTag = tag;
	}
	
	public Object getTag()
	{
		return mItemTag;
	}
	
	public void setItemVisibility(int visibility)
	{
		mItemVisibility = visibility;
	}
	
	public int getItemVisibility()
	{
		return mItemVisibility;
	}
	
	public void setItemView(View view)
	{
		mItemView = view;
	}
	
	public View getItemView()
	{
		return mItemView;
	}
}
