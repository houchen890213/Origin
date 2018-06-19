package com.yulin.common.page;

import android.os.Handler;
import android.view.View;

/**
 * 开屏Page 用于程序的启动界面，或者开屏广告展示 启动类型参考@PageIntent ,SplashPage一般多使用FLAG_PAGE_NO_HISTORY emoney
 *
 */
public class SplashPage extends Page {
    public static boolean isPageExist = false;
    private int mDelayedTime = 3000;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (mSplashListener != null) {
                isPageExist = false;
                mSplashListener.onSplashFinished(mContentView);
            }
        }

    };

    public SplashPage() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void initPage() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onPageCreated() {
        // TODO Auto-generated method stub
        super.onPageCreated();
        isPageExist = true;
    }

    /**
     * 设置延迟时间
     * 
     * @param time
     */
    public void setDelayedTime(int time) {
        mDelayedTime = time;
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        if (mSplashListener != null) {
            mSplashListener.onSplashStarted(mContentView);
        }
        mHandler.postDelayed(mRunnable, mDelayedTime);
    }

    public void setOnSplashListener(OnSplashListener listener) {
        mSplashListener = listener;
    }

    private OnSplashListener mSplashListener = null;

    public static interface OnSplashListener {
        public void onSplashStarted(View view);

        public void onSplashFinished(View view);
    }

    protected void onPageDestroy() {
        super.onPageDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public boolean isAdded2Stack() {
        // TODO Auto-generated method stub
        return isAdded();
    }
}
