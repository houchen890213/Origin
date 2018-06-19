package com.yulin.common.bar;

import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 一组层叠的菜单项
 */
public class BarMenuGroupItem extends BarMenuItem {

	private List<BarMenuItem> mLstItems = new ArrayList<BarMenuItem>();
	private ViewSwitcher mViewSwitcher = null;
	public BarMenuGroupItem() {
		// TODO Auto-generated constructor stub
	}

	public void addItem(BarMenuItem item)
	{
		mLstItems.add(item);
	}
	
	public List<BarMenuItem> getItems()
	{
		return mLstItems;
	}
	
	public void setSwitcher(ViewSwitcher vs)
	{
		mViewSwitcher = vs;
	}
	
	public void showItem(int menuItemId)
	{
		int size = mLstItems.size();
		for(int i = 0; i < size; i++)
		{
			BarMenuItem item = mLstItems.get(i);
			if(menuItemId == item.getItemId())
			{
				mViewSwitcher.setDisplayedChild(i);
				break;
			}
		}
	}
	
	public ViewSwitcher getItemView()
	{
		return (ViewSwitcher) mItemView;
	}
}
