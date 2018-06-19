package com.yulin.common.logger;

import android.util.Log;

/**
 * 输出至Logcat的打印日志
 * 方法参考系统类@Log
 */
public class Tracer {

	public static boolean LOG_DEBUG = false;

	public static void enable()
	{
		LOG_DEBUG = true;
	}
	
	public static void disable()
	{
		LOG_DEBUG = false;
	}
	
	public static void V(String tag, String msg)
	{
		if(!LOG_DEBUG)
		{
			return;
		}
		Log.v(tag, msg);
	}
	public static void E(String tag, String msg)
	{
		if(!LOG_DEBUG)
		{
			return;
		}
		Log.e(tag, msg);
	}
	
	public static void I(String tag, String msg)
	{
		if(!LOG_DEBUG)
		{
			return;
		}
		Log.i(tag, msg);
	}
	
	public static void D(String tag, String msg)
	{
		if(!LOG_DEBUG)
		{
			return;
		}
		Log.d(tag, msg);
	}
}
