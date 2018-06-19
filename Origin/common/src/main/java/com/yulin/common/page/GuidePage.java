package com.yulin.common.page;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序帮助引导page
 * 一般程序第一次启动时，介绍新功能时使用。
 */
public class GuidePage extends Page {

	private ViewPager mViewPager = null;
	private GuidePageAdapter mAdapter = null;
	private List<View> mLstViews = new ArrayList<View>();
	public GuidePage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initPage() {
		mViewPager = new ViewPager(getActivity());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
				if(mGuideListener != null)
				{
					mGuideListener.onGuide(mAdapter.getItem(index), index);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mAdapter = new GuidePageAdapter();
		mViewPager.setAdapter(mAdapter);
		
		setContentView(mViewPager);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if(mAdapter != null)
		{
			mAdapter.notifyDataSetChanged();
		}
	}

	public void addView(View view)
	{
		if(view == null)
		{
			return;
		}
		mLstViews.add(view);
	}
	public void addView(int layoutId)
	{
		View view = View.inflate(getContext(), layoutId, null);
		addView(view);
	}

	class GuidePageAdapter extends PagerAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mLstViews.size();
		}

		public View getItem(int position)
		{
			return mLstViews.get(position);
		}
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(mLstViews.get(arg1));
        }  
  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).addView(mLstViews.get(arg1));
            return mLstViews.get(arg1);  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
	}
	
	private OnGuideListener mGuideListener = null;
	public void setOnGuideListener(OnGuideListener listener)
	{
		mGuideListener = listener;
	}
	public static interface OnGuideListener
	{
		public void onGuide(View view, int index);
	}
	@Override
	public boolean isAdded2Stack() {
		// TODO Auto-generated method stub
		return isAdded();
	}
}
