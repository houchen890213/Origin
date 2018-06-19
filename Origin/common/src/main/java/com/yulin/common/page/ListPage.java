package com.yulin.common.page;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * 列表page，适用于列表菜单等
 * emoney
 *
 */
public abstract class ListPage extends Page{

	private int mBackgroundColor = -1;
	private int mBackgroundResource = -1;
	private int mListCacheColorHint = 0;
	
	private ListView mListView = null;
	private ListAdapter mAdapter = null;
	private List mLstData = null;

	@Override
	final protected void initPage() {
		// TODO Auto-generated method stub
		mListView = new ListView(getActivity());
		if(mBackgroundResource != -1)
		{
			mListView.setBackgroundResource(mBackgroundResource);
		}
		else if(mBackgroundColor != -1)
		{
			mListView.setBackgroundColor(mBackgroundColor);
		}
		mListView.setCacheColorHint(mListCacheColorHint);
		if(mAdapter == null)
		{
			mLstData = getListData();
			mAdapter = new ListPageAdapter();
		}
		mListView.setAdapter(mAdapter);
	    mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long id) {
				// TODO Auto-generated method stub
				ListPage.this.onItemClick(index, id);
			}
		});
	    setContentView(mListView);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	public void setListCacheColorHint(int color) {
		// TODO Auto-generated method stub
		mListCacheColorHint = color;
	}
	
	public void setBackgroundColor(int color) {
		// TODO Auto-generated method stub
		mBackgroundColor = color;
	}
	
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		mAdapter = adapter;
	}
	
	public void onItemClick(int index, long id)
	{
		
	}
	public ListView getListView()
	{
		return mListView;
	}
	public abstract View getView(Object data,int position, View convertView);
	public abstract List getListData();
	class ListPageAdapter extends BaseAdapter
	{
		public ListPageAdapter()
		{
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mLstData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mLstData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return ListPage.this.getView(getItem(position),position, convertView);
		}
		
	}
}
