package com.yulin.common.base.page;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.yulin.common.R;

/**
 * Created by liu_lei on 2017/10/12.
 * 标题栏和任务栏
 */

public class TitleBarPage<T extends ViewDataBinding> extends Page<T> {

    public static final int TITLE_TYPE_DEFAULT = 1;    // toolbar 默认标题
    public static final int TITLE_TYPE_LEFT = 2;       // 自定义标题，靠左显示
    public static final int TITLE_TYPE_CENTER = 3;     // 自定义标题，靠右显示

    private Toolbar mToolbar;                          // 标题栏
    private TextView mTvLeftTitle, mTvCenterTitle;     // 两个标题，靠左、居中
    private TextView mTvClose;                         // 关闭按钮
    private int mTitleType = TITLE_TYPE_CENTER;          // 标题类型

    @Override
    public void initView() {
        formatTitleBar();
    }

    /**
     * 格式化标题栏
     * 1. 任务栏透明
     * 2. 加上padding
     */
    private void formatTitleBar() {
        if (rootLayout != null) {
            mToolbar = (Toolbar) rootLayout.findViewById(R.id.title_bar);
            mTvClose = (TextView) rootLayout.findViewById(R.id.tv_title_bar_close);
            mTvLeftTitle = (TextView) rootLayout.findViewById(R.id.tv_title_bar_title_left);
            mTvCenterTitle = (TextView) rootLayout.findViewById(R.id.tv_title_bar_title_center);

            if (mToolbar != null) {
                mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNavigationIconClick();
                    }
                });
            }

            if (mTvClose != null) {
                mTvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCloseMenuClick(v);
                    }
                });
            }
        }
    }

    protected void onNavigationIconClick() {
    }

    protected void setNavigationIcon(int iconId, View.OnClickListener listener) {
        if (mToolbar != null && iconId > 0 && listener != null) {
            mToolbar.setNavigationIcon(iconId);
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    protected void removeNavigationIcon() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(null);
            mToolbar.setNavigationOnClickListener(null);
        }
    }

    // 显示或隐藏关闭按钮
    protected void updateCloseMenu(boolean isVisible) {
        setViewVisible(mTvClose, isVisible, true);
    }

    // 标题栏关闭点击
    protected void onCloseMenuClick(View v) {
    }

    /**
     * 设置标题类型：默认、靠左、居中
     */
    protected void setTitleType(int type) {
        if (type == TITLE_TYPE_DEFAULT || type == TITLE_TYPE_LEFT || type == TITLE_TYPE_CENTER)
            mTitleType = type;
    }

    protected void setTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle("");
        }
        setViewVisible(mTvLeftTitle, false, true);
        setViewVisible(mTvCenterTitle, false, true);

        switch (mTitleType) {
            case TITLE_TYPE_DEFAULT:
                if (mToolbar != null) mToolbar.setTitle(title);
                break;
            case TITLE_TYPE_LEFT:
                setViewVisible(mTvLeftTitle, true);
                if (mTvLeftTitle != null) mTvLeftTitle.setText(title);
                break;
            case TITLE_TYPE_CENTER:
                setViewVisible(mTvCenterTitle, true);
                if (mTvCenterTitle != null) mTvCenterTitle.setText(title);
                break;
        }
    }

    // 添加menu
    protected Menu addMenu(int menuId, Toolbar.OnMenuItemClickListener listener) {
        if (mToolbar != null && menuId > 0 && listener != null) {
            mToolbar.inflateMenu(menuId);
            mToolbar.setOnMenuItemClickListener(listener);
            return mToolbar.getMenu();
        }

        return null;
    }

}
