package com.yulin.common.base.module;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yulin.common.R;

/**
 * Created by liu_lei on 2017/10/11.
 * 设置状态栏及标题栏
 */

public class TitleBarModule<T extends ViewDataBinding> extends Module<T> {

    public static final int TITLE_TYPE_DEFAULT = 1;    // toolbar 默认标题
    public static final int TITLE_TYPE_LEFT = 2;       // 自定义标题，靠左显示
    public static final int TITLE_TYPE_CENTER = 3;     // 自定义标题，靠右显示

    //是否设置状态栏透明属性
    private boolean isStatusBarTranslucent = true;

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
     * toolbar加上默认导航图标
     */
    private void formatTitleBar() {
        mToolbar = findViewById(R.id.title_bar);
        mTvClose = findViewById(R.id.tv_title_bar_close);
        mTvLeftTitle = findViewById(R.id.tv_title_bar_title_left);
        mTvCenterTitle = findViewById(R.id.tv_title_bar_title_center);

        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationBackPress();
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

    protected void setNavigationIcon(int iconId, View.OnClickListener listener) {
        if (mToolbar != null && iconId > 0 && listener != null) {
            mToolbar.setNavigationIcon(iconId);
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    // 显示或隐藏关闭按钮
    protected void updateCloseMenu(boolean isVisible) {
        setViewVisible(mTvClose, isVisible, true);
    }

    // 标题栏关闭点击
    protected void onCloseMenuClick(View v) {
        finish();
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
            mToolbar.setOnMenuItemClickListener(listener);
            return mToolbar.getMenu();
        }

        return null;
    }

    protected void onNavigationBackPress() {
        finish();
    }

    // 设置沉浸式状态栏
    private void setStatusBarTranslucent() {
        if (!isStatusBarTranslucent) {
            return;
        }

        // 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //不能设置导航栏目透明，导航栏会浮在APP上方导致APP的导航无法使用
                //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 设置根布局的参数
                // ViewGroup rootView = (ViewGroup) ((ViewGroup)
                // activity.findViewById(android.R.id.content)).getChildAt(0);
                // rootView.setFitsSystemWindows(true);
                // rootView.setClipToPadding(true);
                // rootView.setBackgroundColor(Util.getRColor(R.color.c9));
            }
        }
    }

    /**
     * 全屏显示内容，并透明化任务栏
     * 标题栏需要加top padding，padding值为任务栏高度
     * 标题栏可以为toolbar，也可以为其它任务布局
     * 因为内容全屏显示了，所以输入法弹出后界面的缩放效果会失效，所以尽量不用这种方式实现透明任务栏
     */
    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 全屏
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                //取消设置透明状态栏,使 Content 内容不再覆盖状态栏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏颜色
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    public void setStatusBarTranslucent(boolean statusBarTranslucent) {
        isStatusBarTranslucent = statusBarTranslucent;
    }

}
