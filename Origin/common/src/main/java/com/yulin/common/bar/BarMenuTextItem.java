package com.yulin.common.bar;

import android.widget.TextView;


/**
 * 文字菜单项
 */
public class BarMenuTextItem extends BarMenuItem{

	protected String mItemName = null;
	
	private int mTextSize = -1;
	public BarMenuTextItem()
	{

	}
	
	/**
	 * 文字菜单项构造函数
	 * @param id 菜单项id
	 * @param name 菜单名称
	 */
	public BarMenuTextItem(int id, String name)
	{
		mItemId = id;
		mItemName = name;
	}
	
	public String getItemName()
	{
		return mItemName;
	}
	
	public void setItemName(String itemName)
	{
		mItemName = itemName;
	}
	
	public TextView getItemView()
	{
		return (TextView) mItemView;
	}
	
	/**
	 * 
	 * @param textSize 单位dp
	 */
	public void setTextSize(int textSize)
	{
		mTextSize = textSize;
	}
	
	public int getTextSize()
	{
		return mTextSize;
	}
}
