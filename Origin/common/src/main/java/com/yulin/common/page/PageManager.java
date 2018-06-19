package com.yulin.common.page;

import android.support.v4.app.FragmentManager;

import com.yulin.common.logger.Tracer;

import java.util.ArrayList;
import java.util.List;

/**
 * 界面管理类
 * 用于管理page的栈
 * PageManager与@Page 绑定，可以灵活的管理Page之上所有Page
 * 
 * emoney
 * @version 1.0
 * 
 */
public class PageManager {

	private FragmentManager mFragmentManager = null;
	private IntentManager mIntentManager = null;
	
	private final static String TAG = "PageManager";
	public PageManager(FragmentManager manager) {
		mFragmentManager = manager;
		mIntentManager = new IntentManager();
	}
	
	public FragmentManager getFragmentManager()
	{
		return mFragmentManager;
	}
	
	/**
	 * 弹出指定的page
	 * @param intent
	 * @return
	 */
	public boolean popPage(PageIntent intent)
	{
		if(intent == null)
		{
			return false;
		}
		boolean result = mFragmentManager.popBackStackImmediate(intent.getTargetInstance().getStackKey(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		
		return result;
	}
	
	/**
	 * 弹出指定page顶部的所有page
	 * @param intent
	 * @param isInclude 是否包含当前page
	 * @return
	 */
	public boolean popPageTopOf(PageIntent intent,boolean isInclude)
	{
		int size = mIntentManager.getIntentCount();
		int index = mIntentManager.firstIndexOf(intent);
		List<PageIntent> intents = mIntentManager.getIntents();
		if(index >= 0)
		{
			int startIndex = 0;
			if(isInclude)
			{
				startIndex = index;
			}
			else
			{
				startIndex = index + 1;
			}
			boolean hasTopIntent = false;
			List<PageIntent> tempIntents = new ArrayList<PageIntent>();
			for(int i = startIndex; i < size; i++)
			{
				tempIntents.add(intents.get(i));
			}
			for(int i = 0; i < tempIntents.size(); i++)
			{
				if(popPage(tempIntents.get(i)))
				{
//				    unregistIntent(tempIntents.get(i));
					hasTopIntent = true;
				}
			}
			if(hasTopIntent)
			{
				PageIntent oldIntent = mIntentManager.getIntentAt(index);
				oldIntent.getTargetInstance().dispatchNewIntent(intent);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 弹出指定page顶部的所有page,不包含当前page
	 * @param intent
	 * @return
	 */
	public boolean popPageTopOf(PageIntent intent)
	{
		return popPageTopOf(intent, false);
	}
	
	/**
	 * 注册page意图
	 * @param intent
	 */
	public void registIntent(PageIntent intent)
	{
		mIntentManager.registIntent(intent);
	}
	
	/**
	 * 注销page意图
	 * @param intent
	 */
	public void unregistIntent(PageIntent intent)
	{
		mIntentManager.unregistIntent(intent);
	}
	
	/**
	 * 是否打印日志
	 * @param flag
	 */
	public void needPringLog(boolean flag)
	{
		mIntentManager.needPringLog(flag);
	}
	
	
	public List<PageIntent> getIntents()
	{
		return mIntentManager.getIntents();
	}
	
	/**
	 * 获取栈顶的page意图
	 * @return
	 */
	public PageIntent getTopIntent()
	{
		return mIntentManager.getTopIntent();
	}
	
	/**
	 * 获取栈中某项的page意图
	 * @return
	 */
	public PageIntent getIntentAt(int index)
	{
		return mIntentManager.getIntentAt(index);
	}
	
	/**
	 * 获取栈中是否有某意图
	 * @return
	 */
	public boolean hasIntent(PageIntent pageIntent)
	{
		return mIntentManager.hasIntent(pageIntent);
	}

	
	/**
	 * 获取栈中page意图数量
	 * @return
	 */
	public int getIntentSize()
	{
		return mIntentManager.getIntentCount();
	}
	
	/**
	 * page意图管理类
	 * 用于管理page的意图
	 * emoney
	 *
	 */
	static class IntentManager {

		private List<PageIntent> mLstIntents = new ArrayList<PageIntent>();

		private boolean mNeedPrintLog = false;
		public IntentManager() {
			// TODO Auto-generated constructor stub
		}

		public void registIntent(PageIntent intent)
		{
			if(intent == null)
			{
				return;
			}
			if(intent.getFlags() != PageIntent.FLAG_PAGE_NO_HISTORY
					&& intent.getFlags() != PageIntent.FLAG_PAGE_REPLACE)
			{
				mLstIntents.add(intent);
				printLog("新增意图,内容:"+intent);
			}
		}
		public void unregistIntent(PageIntent intent)
		{
			if(intent == null)
			{
				return;
			}
			if(mLstIntents.contains(intent))
			{
				mLstIntents.remove(intent);
				printLog("注销意图,内容:"+intent);
			}
		}
		
		public int firstIndexOf(PageIntent pageIntent)
		{
			int size = getIntentCount();
			int index = -1;
			for(int i = 0; i < size; i++)
			{
				PageIntent intent = mLstIntents.get(i);
				if(intent.equals(pageIntent))
				{
					index = i;
					break;
				}
			}
			return index;
		}

		public PageIntent getTopIntent()
		{
			int size = mLstIntents.size();
			if(size > 0)
			{
				return mLstIntents.get(size - 1);
			}
			return null;
		}
		
		public List<PageIntent> getIntents()
		{
			return mLstIntents;
		}
		
		public PageIntent getIntentAt(int index)
		{
			return mLstIntents.get(index);
		}
		public int getIntentCount()
		{
			return mLstIntents.size();
		}
		
		private void printLog(String msg)
		{
			if(mNeedPrintLog)
			{
				Tracer.V(TAG, msg);
			}
		}
		
		public void needPringLog(boolean flag)
		{
			mNeedPrintLog = flag;
		}
		
		public boolean hasIntent(PageIntent pageIntent)
		{
			int size = getIntentCount();
			for(int i = 0; i < size; i++)
			{
				PageIntent intent = mLstIntents.get(i);
				if(intent.equals(pageIntent))
				{
					return true;
				}
			}
			return false;
		}

	}
}
