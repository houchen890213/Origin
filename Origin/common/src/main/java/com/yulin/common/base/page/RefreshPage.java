package com.yulin.common.base.page;

import android.databinding.ViewDataBinding;
import android.support.v4.widget.SwipeRefreshLayout;

import com.yulin.common.R;

/**
 * Created by liu_lei on 2017/10/12.
 * 内置有下拉刷新的page
 */

public class RefreshPage<T extends ViewDataBinding> extends TitleBarPage<T> implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isAutoPullDownRefresh = true;    // 默认为true

    @Override
    public void initView() {
        super.initView();

        swipeRefreshLayout = rootLayout.findViewById(R.id.swipe_refresh_layout);

        if (swipeRefreshLayout != null) {
            int[] colors = context.getResources().getIntArray(R.array.swipe_refresh_colors);
            swipeRefreshLayout.setColorSchemeColors(colors);
            swipeRefreshLayout.setOnRefreshListener(this);
        }

        setPullDownRefreshEnabled(true);
    }

    @Override
    public void initData() {
        super.initData();

        if (isAutoPullDownRefresh) {
            startAutoRefresh();
        }
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 设置是否启用下拉刷新
     *
     * @param enabled 是否允许下拉刷新
     */
    public void setPullDownRefreshEnabled(boolean enabled) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setEnabled(enabled);
    }

    /**
     * 设置是否生命周期initData自动下拉刷新
     *
     * @param autoPullDownRefresh 是否自动下拉刷新
     */
    public void setAutoPullDownRefresh(boolean autoPullDownRefresh) {
        isAutoPullDownRefresh = autoPullDownRefresh;
    }

    /**
     * initData时，如果允许自动刷新，启动一次刷新
     */
    private void startAutoRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
            }, 200);
        }
    }

    public void refreshEnd() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
