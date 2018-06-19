package com.yulin.common.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 导航栏
 * 点击某一项，居中显示
 * emoney
 *
 */
public class NavigateBar extends Bar {

	private LinearLayout mContent = null;
	private LinearLayout mMainContent = null;
	private HorizontalScrollView mScrollView = null;
	private ImageView mIvLeft = null;
	private ImageView mIvRight = null;
	
	private int mItemSpace = 0;
	public NavigateBar(Context context) {
		super(context);
		init();
	}

	public NavigateBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NavigateBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init()
	{
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.HORIZONTAL);
		mContent.setLayoutParams(params);
		
		mIvLeft = new ImageView(getContext());
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mIvLeft.setLayoutParams(p);
		
		mIvRight = new ImageView(getContext());
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mIvRight.setLayoutParams(p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		p.weight = 1;
		mMainContent = new LinearLayout(getContext());
		mMainContent.setOrientation(LinearLayout.HORIZONTAL);
		mMainContent.setLayoutParams(p);
		
		mContent.addView(mIvLeft);
		mContent.addView(mMainContent);
		mContent.addView(mIvRight);
		
		mScrollView = new HorizontalScrollView(getContext());
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mScrollView.setLayoutParams(params);
		mScrollView.setHorizontalScrollBarEnabled(false);
		
		mScrollView.addView(mContent);
		
		addView(mScrollView);
	}
	
	public void setCurrentItem(int index)
	{
		if(mCurrIndex == index)
		{
			return;
		}
		startSliding(index);
	}
	
	private void startSliding(int index)
	{
		if(mCurrIndex == index)
		{
			return;
		}
		BarMenuItem lastMenuItem = getBarMenu().getItem(mCurrIndex);
		BarMenuItem menuItem = getBarMenu().getItem(index);
		View lastItem = lastMenuItem.getItemView();
		View tvItem = menuItem.getItemView();
		int startX = tvItem.getLeft() + tvItem.getMeasuredWidth()/2;
		int endX = mScrollView.getScrollX() + mScrollView.getMeasuredWidth()/2;
		float toX = startX - endX;
		mScrollView.smoothScrollBy((int)toX, 0);
		if(isItemSelectable())
		{
			if(lastItem instanceof TextView)
			{
				TextView tv = (TextView) lastItem;
				tv.setTextColor(mItemTextColor);
			}
			lastItem.setSelected(false);
			if(tvItem instanceof TextView)
			{
				TextView tv = (TextView) tvItem;
				tv.setTextColor(mItemSelectedTextColor);
			}
			tvItem.setSelected(true);
		}
		mCurrIndex = index;
	}
	
	public void setImageLeft(int resId, OnClickListener listener)
	{
		if(mIvLeft != null)
		{
			mIvLeft.setImageResource(resId);
			mIvLeft.setOnClickListener(listener);
		}
	}

	public void setImageRight(int resId, OnClickListener listener)
	{
		if(mIvRight != null)
		{
			mIvRight.setImageResource(resId);
			mIvRight.setOnClickListener(listener);
		}
	}
	
	private void addDivider()
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDividerWidth, LinearLayout.LayoutParams.FILL_PARENT);
		View view = createDivider(params);
		
		mMainContent.addView(view);
	}
	
	public void addMenuItemNow(BarMenuItem item, int index)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		if(index < getItemCount())
		{
			int halfSpace = mItemSpace/2;
			params.rightMargin = halfSpace;
			params.leftMargin = halfSpace;
		}
		if(item instanceof BarMenuTextItem)
		{
			BarMenuTextItem textItem = (BarMenuTextItem) item;
			mMainContent.addView(createTextItem(textItem, index, params));
		}
		else if(item instanceof BarMenuImgItem)
		{
			BarMenuImgItem imgItem = (BarMenuImgItem) item;
			mMainContent.addView(createImageItem(imgItem, index, params));
		}
	}
	
	public void addMenuItemNow(BarMenuItem item)
	{
		int count = mMainContent.getChildCount();
		addMenuItemNow(item, count);
	}
	public void setItemSpace(int space)
	{
		mItemSpace = space;
	}
	@Override
	public void notifyBarSetChanged() {
		mMainContent.removeAllViews();
		mMainContent.removeAllViewsInLayout();
		BarMenu menu = getBarMenu();
		List<BarMenuItem> items = menu.getItems();
		if(isDividerEnabled())
		{
			for(int i = 0; i < items.size(); i++)
			{
				final BarMenuItem item = items.get(i);
				addMenuItemNow(item, i);
				
				if(i != items.size() - 1)
				{
					addDivider();
				}
			}
		}
		else
		{
			for(int i = 0; i < items.size(); i++)
			{
				final BarMenuItem item = items.get(i);
				addMenuItemNow(item, i);
			}

		}
	}
}
