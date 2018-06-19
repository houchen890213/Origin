package com.yulin.common.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 工具栏 用于一组界面或者一组功能按钮的呈现和组织
 * 
 * emoney
 *
 */
public class ToolBar extends Bar {

	private LinearLayout mContent = null;
	private LinearLayout mSliderContent = null;
	private ImageView mIvSlider = null;

	private float mCurrRate = 0;

	public ToolBar(Context context) {
		super(context);
		init();
	}

	public ToolBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		mContent = new LinearLayout(getContext());
		mContent.setLayoutParams(params);
		mContent.setOrientation(LinearLayout.HORIZONTAL);
		mContent.setGravity(Gravity.CENTER_VERTICAL);
		mSliderContent = new LinearLayout(getContext());
		mSliderContent.setLayoutParams(params);
		mSliderContent.setGravity(Gravity.CENTER_VERTICAL);
		mIvSlider = new ImageView(getContext());
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		mIvSlider.setLayoutParams(params);

		mSliderContent.addView(mIvSlider);
		addView(mSliderContent);
		addView(mContent);
	}

	public void setItemsOrientation(int orientation) {
		mContent.setOrientation(orientation);
	}

	public void setItemsGravity(int gravity) {
		mContent.setGravity(gravity);
	}

	public void setSliderImageResource(int resId) {
		mIvSlider.setImageResource(resId);
	}

	public void setSliderBackgroundResource(int resId) {
		mIvSlider.setBackgroundResource(resId);
	}

	public void setSliderBackgroundColor(int color) {
		mIvSlider.setBackgroundColor(color);
	}

	public void setCurrentItem(int index) {
		if (mCurrIndex == index) {
			return;
		}
		if (isItemSelectable()) {
			BarMenuItem lastMenuItem = getBarMenu().getItem(mCurrIndex);
			View lastItem = lastMenuItem.getItemView();
			if (lastItem instanceof TextView) {
				TextView tv = (TextView) lastItem;
				tv.setTextColor(mItemTextColor);
			}
			lastItem.setSelected(false);

			BarMenuItem menuItem = getBarMenu().getItem(index);
			View currItem = menuItem.getItemView();
			if (currItem instanceof TextView) {
				TextView tv = (TextView) currItem;
				tv.setTextColor(mItemSelectedTextColor);
			}
			currItem.setSelected(true);
		}

		int width = getMeasuredWidth();
		int oneWidth = width / getItemCount();
		mIvSlider.layout(index * oneWidth, 0, (index + 1) * oneWidth, getMeasuredHeight());
		mCurrIndex = index;
	}

	/**
	 * 设置当前选中的item，并传入滑动过程中的位置比例，多用于ViewPager
	 * 
	 * @param index
	 * @param rate
	 */
	public void setCurrentItem(int index, float rate) {
		mCurrRate = rate;
		int width = getMeasuredWidth();
		int oneWidth = width / getItemCount();
		int offset = (int) (rate * oneWidth);

		mIvSlider.layout(index * oneWidth + offset, 0, (index + 1) * oneWidth + offset, getMeasuredHeight());
		if (rate == 0) {
			if (isItemSelectable()) {
				BarMenuItem lastMenuItem = getBarMenu().getItem(mCurrIndex);
				View lastItem = lastMenuItem.getItemView();
				if (lastItem instanceof TextView) {
					TextView tv = (TextView) lastItem;
					tv.setTextColor(mItemTextColor);
				}
				lastItem.setSelected(false);

				BarMenuItem menuItem = getBarMenu().getItem(index);
				View currItem = menuItem.getItemView();
				if (currItem instanceof TextView) {
					TextView tv = (TextView) currItem;
					tv.setTextColor(mItemSelectedTextColor);
				}
				currItem.setSelected(true);
			}
			mCurrIndex = index;
		}
	}

	private void startSliding(final int startIndex, final int endIndex) {
		int width = getMeasuredWidth();
		int oneWidth = width / getItemCount();
		float fromX = 0;
		float toX = (endIndex - startIndex) * oneWidth;
		float fromY = 0;
		float toY = 0;
		TranslateAnimation anim = new TranslateAnimation(fromX, toX, fromY, toY);
		anim.setDuration(500);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				TextView lastItem = (TextView) mContent.getChildAt(mCurrIndex);
				lastItem.setTextColor(mItemTextColor);
				TextView currItem = (TextView) mContent.getChildAt(endIndex);
				currItem.setTextColor(mItemSelectedTextColor);
				mIvSlider.clearAnimation();
				int width = getMeasuredWidth();
				int oneWidth = width / getItemCount();
				mIvSlider.layout(endIndex * oneWidth, 0, (endIndex + 1) * oneWidth, mContent.getMeasuredHeight());
				mCurrIndex = endIndex;
			}
		});
		anim.setFillAfter(true);
		mIvSlider.startAnimation(anim);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);

		int count = getItemCount();
		if (count > 0) {
			int width = r - l;
			int height = b - t;
			int oneWidth = width / count;
			mContent.layout(0, 0, width, height);
			mSliderContent.layout(0, 0, width, height);
			if (mCurrRate == 0) {
				mIvSlider.layout(mCurrIndex * oneWidth, 0, (mCurrIndex + 1) * oneWidth, height);
			}
		}
	}

	private void addDivider() {
		LinearLayout.LayoutParams params = null;
		int orientation = mContent.getOrientation();
		if (orientation == LinearLayout.VERTICAL) {
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mDividerWidth);
		} else {
			params = new LinearLayout.LayoutParams(mDividerWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		}
		View view = createDivider(params);

		mContent.addView(view);
	}

	public void addMenuItemNow(BarMenuItem item, int index) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		params.weight = 1;

		if (item instanceof BarMenuTextItem) {
			BarMenuTextItem textItem = (BarMenuTextItem) item;
			TextView tv = createTextItem(textItem, index, params);
			mContent.addView(tv);

			textItem.setItemView(tv);
		} else if (item instanceof BarMenuImgItem) {
			BarMenuImgItem imgItem = (BarMenuImgItem) item;
			mContent.addView(createImageItem(imgItem, index, params));
		} else if (item instanceof BarMenuCustomItem) {
			LinearLayout.LayoutParams paramsItem = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			BarMenuCustomItem customItem = (BarMenuCustomItem) item;
			mContent.addView(createCustomItem(customItem, index, paramsItem), params);
		}

	}

	public void addMenuItemNow(BarMenuItem item) {
		int count = mContent.getChildCount();
		addMenuItemNow(item, count);
	}

	@Override
	public void notifyBarSetChanged() {
		mContent.removeAllViews();
		mContent.removeAllViewsInLayout();
		BarMenu menu = getBarMenu();
		List<BarMenuItem> items = menu.getItems();
		if (isDividerEnabled()) {
			for (int i = 0; i < items.size(); i++) {
				final BarMenuItem item = items.get(i);
				addMenuItemNow(item, i);

				if (i != items.size() - 1) {
					addDivider();
				}
			}
		} else {
			for (int i = 0; i < items.size(); i++) {
				final BarMenuItem item = items.get(i);
				addMenuItemNow(item, i);
			}
		}
	}
}
