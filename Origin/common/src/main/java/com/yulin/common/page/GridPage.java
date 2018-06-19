package com.yulin.common.page;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.List;

/**
 * 网格page，适用于九宫格界面等
 *
 */
public abstract class GridPage extends Page {
	private int mBackgroundColor = -1;
	private int mBackgroundResource = -1;
	private int mListCacheColorHint = 0;
	private int mHSpace = 5;
	private int mVSpace = 5;
	
	private GridView mGridView = null;
	private ListAdapter mAdapter = null;
	private List mGridData = null;
	
	private int mNumColumns = 3;
	private int mGravity = Gravity.CENTER;
	public GridPage() {
		// TODO Auto-generated constructor stub
	}

	public void setGravity(int gravity)
	{
		mGravity = gravity;
	}
	/**
	 * 设置列数
	 * @param col
	 */
	public void setNumColumns(int col)
	{
		mNumColumns = col;
	}
	
	/**
	 * @param color
	 */
	public void setListCacheColorHint(int color) {
		// TODO Auto-generated method stub
		mListCacheColorHint = color;
	}
	
	/**
	 * @param color
	 */
	public void setBackgroundColor(int color) {
		// TODO Auto-generated method stub
		mBackgroundColor = color;
	}
	
	/**
	 * 设置水平间距
	 * @param space
	 */
	public void setHorizontalSpacing(int space)
	{
		mHSpace = space;
	}
	
	/**
	 * 设置垂直间距
	 * @param space
	 */
	public void setVerticalSpacing(int space)
	{
		mVSpace = space;
	}
	
	@Override
	final protected void initPage() {
		// TODO Auto-generated method stub
		mGridView = new GridView(getActivity());
		if(mBackgroundResource != -1)
		{
			mGridView.setBackgroundResource(mBackgroundResource);
		}
		else if(mBackgroundColor != -1)
		{
			mGridView.setBackgroundColor(mBackgroundColor);
		}
		mGridView.setCacheColorHint(mListCacheColorHint);
		mGridView.setGravity(mGravity);
		mGridView.setNumColumns(mNumColumns);
		mGridView.setHorizontalSpacing(mHSpace);
		mGridView.setVerticalSpacing(mVSpace);
		if(mAdapter == null)
		{
			mGridData = getGridData();
			mAdapter = new GridPageAdapter();
		}
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long id) {
				// TODO Auto-generated method stub
				GridPage.this.onItemClick(mGridData.get(index),index, id);
			}
		});
		
		setContentView(mGridView);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 点击事件
	 * @param data 数据
	 * @param index 索引
	 * @param id
	 */
	public void onItemClick(Object data, int index, long id)
	{
		
	}
	public GridView getGridView()
	{
		return mGridView;
	}
	public abstract View getView(Object data,int position, View convertView);
	public abstract List getGridData();
	class GridPageAdapter extends BaseAdapter
	{
		public GridPageAdapter()
		{
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mGridData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mGridData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return GridPage.this.getView(getItem(position),position, convertView);
		}
		
	}
}
