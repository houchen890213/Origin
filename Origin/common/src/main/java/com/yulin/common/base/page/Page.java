package com.yulin.common.base.page;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yulin.common.annotation.Content;
import com.yulin.common.base.i.ILifeCycle;
import com.yulin.common.base.page.i.IPageAnimation;
import com.yulin.common.base.module.Module;
import com.yulin.common.base.page.i.OnPageViewListener;
import com.yulin.common.base.page.i.PageContext;
import com.yulin.common.listener.IPresenter;
import com.yulin.common.logger.Logger;
import com.yulin.common.utils.ActivityUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by liu_lei on 2017/10/11.
 * 自定义的fragment基类，基于data_binding
 * 功能：1. 查找布局并加载， 2. 实现生命周期方法， 3. 实现presenter， 4. 实现一些公共方法
 * 5. 实现页面切换动画
 */

public class Page<T extends ViewDataBinding> extends RxFragment implements ILifeCycle, IPresenter, IPageAnimation, View.OnTouchListener, PageContext {

    // 添加寻找layout时，向上遍历的终止位置
    private static final HashSet<Class<?>> END_POINT = new HashSet<>();

    // 静态代码块在类加载时执行
    static {
        END_POINT.add(Module.class);
    }

    protected Context context;
    protected T binding;
    protected View rootLayout;    // 根布局
    public int mContainerId = 0;// page显示的容器View id

    private String mPageTag = null;

    public PageContext mLinkedContext = null;    // 启动起该page的Page
    private PageIntent mPageIntent = null;

    private boolean mNeedPrintLog = false;// 是否打印page生命周期相关日志

    /**
     * page的界面状态
     */
    private enum PageStatus {
        UNCREATE, // 未创建状态
        CREATE, // 初始状态
        PAUSE, // 暂停状态
        RESUMED, // 恢复/运行状态
        DESTROY// 销毁状态
    }

    private LayoutInflater mInflater = null;

    private PageStatus mPageStatus = PageStatus.UNCREATE;

    private List<OnPageViewListener> mLstPageViewListeners = new ArrayList<>();

    private int mResultCode = -1;//
    private int mRequestCode = -1;
    private Bundle mResultData = null;

    private Page mParentPage = null;// 对于嵌套在视图中的page，必须有个依赖的page作为父page

    @Override
    public final void onAttach(Activity activity) {
        if (mLinkedContext != null) {
            // 如果启动起本page的page在切换时，需要pause和resume，就调用它的pause方法
            if (mLinkedContext.needPauseAndResumeWhenSwitch()) {
                mLinkedContext.dispatchPagePause(false);
            }
        }
        super.onAttach(activity);
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;

        if (getLayoutId() > 0) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            rootLayout = binding.getRoot();

            receiveData(getArguments());
            initView();
            initListener();

            return rootLayout;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dispatchPageCreated();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mParentPage != null) {
            if (mParentPage.getUserVisibleHint() && getUserVisibleHint()) {
                dispatchPageResume(true);
            }
        } else {
            if (getUserVisibleHint()) {
                dispatchPageResume(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mParentPage != null) {
            if (mParentPage.getUserVisibleHint() && getUserVisibleHint()) {
                dispatchPagePause(true);
            }
        } else {
            if (getUserVisibleHint()) {
                dispatchPagePause(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dispatchPageDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // 防止出现No Activity的错误
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (mLinkedContext != null && mLinkedContext.isAdded2Stack()) {

            if (mRequestCode >= 0 && mResultCode >= 0) {
                mLinkedContext.dispatchPageResult(mRequestCode, mResultCode, mResultData);
            }
            if (mLinkedContext.needPauseAndResumeWhenSwitch()) {
                mLinkedContext.dispatchPageResume(false);
            }
        }
    }

    /**
     * 接收onNewIntent传入的数据 page已存在在栈中时，收到PageIntent，执行该方法
     */
    protected void receiveNewData(PageIntent intent) {
    }

    /**
     * 接收onCreate传入的数据，只在创建page时调用一次
     */
    protected void receiveData(Bundle bundle) {
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
    }

    /**
     * page界面构建完毕时触发
     * 在initView(), initListener(), initData()之后执行
     */
    protected void onPageCreated() {
        getPageManager().registIntent(mPageIntent);
    }

    /**
     * page恢复状态时触发
     */
    protected void onPageResume() {}

    /**
     * page暂停时触发
     */
    protected void onPagePause() {}

    /**
     * page被销毁时触发
     */
    protected void onPageDestroy() {
        destroyPagesInView();
        PageManager pageManager = getPageManager();
        if (pageManager != null && mPageIntent != null) {
            pageManager.unregistIntent(mPageIntent);
        }
    }

    @Override
    public void onViewClick(View view) {
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
    }

    private int getLayoutId() {
        try {
            Content contentView = getContentLayout(getClass());
            if (contentView != null) {
                return contentView.value();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

        return 0;
    }

    private Content getContentLayout(Class<?> clz) {
        if (clz == null || END_POINT.contains(clz)) {
            return null;
        }

        Content content = clz.getAnnotation(Content.class);

        if (content == null) {
            return getContentLayout(clz.getSuperclass());
        }

        return content;
    }

    /**
     * 弹出toast
     */
    protected void toast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转activity
     */
    protected void next(Class<? extends Activity> clz) {
        ActivityUtil.next(context, clz);
    }

    /**
     * 设置view是否显示
     */
    protected void setViewVisible(View view, boolean isVisible) {
        setViewVisible(view, isVisible, false);
    }

    /**
     * 设置view是否显示
     */
    protected void setViewVisible(View view, boolean isVisible, boolean isGone) {
        if (view != null) {
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                if (isGone) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 有页面入栈时的进入动画
     */
    @Override
    public int enterAnimation() {
        return 0;
    }

    /**
     * 有页面入栈时的退出动画
     */
    @Override
    public int exitAnimation() {
        return 0;
    }

    /**
     * 出栈时的进入动画
     */
    @Override
    public int popEnterAnimation() {
        return 0;
    }

    /**
     * 出栈时的退出动画
     */
    @Override
    public int popExitAnimation() {
        return 0;
    }

    // 防止onTouch事件泄露到底层page
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public PageIntent getPageIntent() {
        return mPageIntent;
    }

    /**
     * page界面构建完毕时触发
     */
    @Override
    public void dispatchPageCreated() {
        mPageStatus = PageStatus.CREATE;
        onPageCreated();
    }

    @Override
    public void dispatchPageResume(boolean isFromSystem) {
        mPageStatus = PageStatus.RESUMED;
        onPageResume();
        if (getUserVisibleHint() && !isFromSystem) {
            for (int i = 0; i < mLstPageViewListeners.size(); i++) {
                mLstPageViewListeners.get(i).onViewResume();
            }
        }
    }

    @Override
    public void dispatchPagePause(boolean isFromSystem) {
        mPageStatus = PageStatus.PAUSE;
        onPagePause();
        if (/* getUserVisibleHint() && */!isFromSystem) {
            for (int i = 0; i < mLstPageViewListeners.size(); i++) {
                mLstPageViewListeners.get(i).onViewPause();
            }
        }
    }

    /**
     * 销毁界面 清除数据，停止网络请求，停止线程等操作
     */
    @Override
    public void dispatchPageDestroy() {
        mPageStatus = PageStatus.DESTROY;
        onPageDestroy();
    }

    @Override
    public void dispatchNewIntent(PageIntent intent) {
        onNewIntent(intent);
    }

    @Override
    public void dispatchPageResult(int requestCode, int resultCode, Bundle data) {
        onPageResult(requestCode, resultCode, data);
    }

    /**
     * 获取上一个page提交的数据
     *
     * @param resultCode 结果code
     * @param data 数据
     */
    protected void onPageResult(int requestCode, int resultCode, Bundle data) {
        if (getUserVisibleHint()) {
            for (int i = 0; i < mLstPageViewListeners.size(); i++) {
                mLstPageViewListeners.get(i).onViewResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 启动page
     *
     * @param containerId page所依赖的view id
     * @param intent page意图
     */
    @Override
    public void startPage(int containerId, PageIntent intent) {
        Module EMActivity = getAct();
        if (EMActivity != null) {
            EMActivity.startPage(containerId, intent);
        }
    }

    @Override
    public void startPage(PageIntent intent) {
        int containerId = mContainerId;
        if (mContainerId == 0) {
            containerId = mParentPage.getContainerId();
        }
        startPage(containerId, intent);
    }

    /**
     * 启动page，要求返回结果
     *
     * @param containerId page所依赖的view id
     * @param intent page意图
     * @param requestCode 请求code
     */
    @Override
    public void startPageForResult(int containerId, PageIntent intent, int requestCode) {
        Module EMActivity = getAct();
        if (EMActivity != null) {
            EMActivity.startPageForResult(containerId, intent, requestCode);
        }
    }

    /**
     * 启动page，要求返回结果
     *
     * @param intent page意图
     * @param requestCode 请求code
     */
    @Override
    public void startPageForResult(PageIntent intent, int requestCode) {
        int containerId = mContainerId;
        if (mParentPage != null) {
            containerId = mParentPage.getContainerId();
        }
        startPageForResult(containerId, intent, requestCode);
    }

    /**
     * 启动模块
     *
     * @param clz 模块的类别
     */
    public void startAct(Class<? extends Module> clz) {
        Module emActivity = getAct();
        if (emActivity != null) {
            emActivity.startAct(clz);
        }
    }

    /**
     * 启动模块
     *
     * @param bundle 需要传递数据
     * @param clz 模块的类别
     */
    public void startAct(Bundle bundle, Class<? extends Module> clz) {
        Module emActivity = getAct();
        if (emActivity != null) {
            emActivity.startAct(bundle, clz);
        }
    }

    /**
     * 启动模块 要求返回结果
     *
     * @param bundle 需要传递数据
     * @param clz 模块的类别
     * @param reqCode 启动页面的code
     */
    public void startActForResult(Bundle bundle, Class<? extends Module> clz, int reqCode) {
        Module EMActivity = getAct();
        if (EMActivity != null) {
            EMActivity.startActForResult(bundle, clz, reqCode);
        }
    }

    /**
     * 结束当前page
     */
    public void finish() {
        if (mParentPage != null) {
            mParentPage.finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() <= 1) {
                if (mLinkedContext == null) {
                    finishAct();
                } else {
                    if (mLinkedContext.isAdded2Stack()) {
                        getFragmentManager().popBackStack(getStackKey(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } else {
                        finishAct();
                    }
                }
            } else {
                try {
                    getFragmentManager().popBackStack(getStackKey(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (IllegalStateException ignored) {
                    // There's no way to avoid getting this if saveInstanceState has already been called.
                }
            }
        }
    }


    public void finishAct() {
        Module EMActivity = getAct();
        if (EMActivity != null) {
            EMActivity.finish();
            //EMActivity.overridePendingTransition(0,0);
        }
    }

    public void finishToPage(PageIntent intent) {
        Module EMActivity = getAct();
        if (EMActivity != null) {
            EMActivity.finishToPage(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        for (int i = 0; i < mLstPageViewListeners.size(); i++) {
            if (mLstPageViewListeners.get(i).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        for (int i = 0; i < mLstPageViewListeners.size(); i++) {
            if (mLstPageViewListeners.get(i).onKeyUp(keyCode, event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean needPauseAndResumeWhenSwitch() {
        return true;
    }

    @Override
    public boolean isAdded2Stack() {
        return isAdded();
    }

    public void needPrintLog(boolean flag) {
        mNeedPrintLog = flag;
    }

    /**
     * 唯一的栈标记
     */
    public String getStackKey() {
        return String.valueOf(hashCode());
    }

    public PageManager getPageManager() {
        Module EMActivity = getAct();
        if (EMActivity != null) {
            return EMActivity.getPageManager();
        }
        return null;
    }

    public Page getParent() {
        return mParentPage;
    }

    public Context getContext() {
        if (mInflater == null) {
            return getActivity();
        }
        return mInflater.getContext();
    }

    /**
     * 获取当前page依赖的模块
     */
    public Module getAct() {
        Activity activity = getActivity();
        if (activity == null) {
            if (mParentPage != null) {
                activity = mParentPage.getActivity();
            }
        }
        if (activity instanceof Module) {
            return (Module) activity;
        }

        return null;
    }

    public void setParent(Page page) {
        mParentPage = page;
    }

    /**
     * 将page转化为普通的view 拥有与page除生命周期意外的其他各种资源
     *
     * @param parent
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View convertToView(Page parent, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setParent(parent);
        return onCreateView(inflater, container, savedInstanceState);
    }

    protected void destroyPagesInView() {
        if (mLstPageViewListeners != null) {
            for (int i = 0; i < mLstPageViewListeners.size(); i++) {
                mLstPageViewListeners.get(i).onViewDestroy();
            }
        }
        if (mLstPageViewListeners != null)
            mLstPageViewListeners.clear();
    }

    public int getContainerId() {
        return mContainerId;
    }

    public void setPageIntent(PageIntent intent) {
        mPageIntent = intent;
    }

    protected void onNewIntent(PageIntent intent) {
        receiveNewData(intent);
    }

    /**
     * 设置requestCode
     *
     * @param requestCode
     */
    public void setRequest(int requestCode) {
        mRequestCode = requestCode;
    }

    /**
     * 取得requestCode
     *
     * @return 上个page传送的reauestCode
     */
    public int getRequest() {
        return mRequestCode;
    }

    /**
     * 设置结果code
     *
     * @param resultCode
     */
    public void setResult(int resultCode) {
        mResultCode = resultCode;
    }

    /**
     * 设置结果code与数据
     *
     * @param resultCode 结果code
     * @param data 数据
     */
    public void setResult(int resultCode, Bundle data) {
        mResultCode = resultCode;
        mResultData = data;
    }

    /**
     * 对用户来说是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);

            if (mPageStatus == PageStatus.UNCREATE) {
                return;
            }
            if (isVisibleToUser) {
                dispatchPageResume(false);
            } else {
                dispatchPagePause(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将包含Page的view类型的视图控件(@PageSwitcher, @PageFlipper等)注册到当前page， 让当前page管理view中包含的所有Page，包括生命周期
     *
     * @param listener
     */
    public void registViewWithPage(OnPageViewListener listener) {
        if (listener == null) {
            return;
        }
        if (!mLstPageViewListeners.contains(listener)) {
            listener.registToPage(this);
            mLstPageViewListeners.add(listener);
        }
    }

    public View getContentView() {
        return rootLayout;
    }

    /**
     * 获取缓存文件夹
     */
    public File getCacheDir() {
        Module EMActivity = getAct();
        if (EMActivity != null) {
            return EMActivity.getCacheDir();
        }
        return null;
    }

}
