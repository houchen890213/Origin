package com.yulin.common.page;

import android.annotation.SuppressLint;

import com.yulin.common.bar.Bar;
import com.yulin.common.bar.BarMenu;
import com.yulin.common.bar.BarMenuItem;
import com.yulin.common.bar.TitleBar;
import com.yulin.common.page.i.TitleBarI;

public abstract class TitlebarPage extends Page implements TitleBarI {
    private int mTitlebarId = -1;
    private boolean bNeedUpdate = false;
    private TitleBar mTitleBar = null;


    public Bar getTitleBar() {
        return mTitleBar;
    }

    /**
     * 绑定page titebar 即拥有该bar的所有控制权
     *
     * @param barId
     */
    public void bindTitleBar(int barId) {
        if (barId != mTitlebarId) {
            mTitlebarId = barId;
            mTitleBar = (TitleBar) findViewById(mTitlebarId);

            bNeedUpdate = true;
        }
    }

    /**
     * 解除绑定bar titebar 解除对该bar的所有控制权
     *
     * @param barId
     */
    public void unbindTitleBar(int barId) {
        mTitlebarId = -1;
    }

    /**
     * 获取所有被绑定的bar
     *
     * @return
     */
    public int getBoundBarId() {
        return mTitlebarId;
    }

    /**
     * 更新已绑定的bar
     */
    @SuppressLint("NewApi")
    private void onUpdateTitleBars() {
        if (bNeedUpdate == false) {
            return;
        }
        bNeedUpdate = false;
        if (getContentView() == null) {
            return;
        }
        if (mTitlebarId > 0) {
            if (!getUserVisibleHint()) {
                return;
            }

            mTitleBar = (TitleBar) findViewById(mTitlebarId);

            if (mTitleBar != null) {
                mTitleBar.initTranslucentBar();
                BarMenu menu = new BarMenu();
                boolean bRet = onCreatePageTitleBarMenu(mTitleBar, menu);
                if (bRet) {
                    mTitleBar.clearBarMenu();
                    mTitleBar.addMenuItems(menu.getItems());
                    mTitleBar.setOnBarMenuSelectedListener(new Bar.OnBarMenuSelectedListener() {
                        @Override
                        public void onItemSelected(int index, BarMenuItem item) {
                            onPageTitleBarMenuItemSelected(item);
                        }
                    });

                    mTitleBar.notifyBarSetChanged();
                }
            }
        }
    }

    /**
     * 标题菜单选中事件回调
     *
     * @param menuitem
     */
    public void onPageTitleBarMenuItemSelected(BarMenuItem menuitem) {

    }

    /**
     * 创建标题菜单开始事件回调
     *
     * @param menu
     */
    public boolean onCreatePageTitleBarMenu(Bar bar, BarMenu menu) {
        return false;
    }


    @Override
    protected void onPageResume() {
        super.onPageResume();
        if (getUserVisibleHint()) {
            onUpdateTitleBars();
        }
    }
}
