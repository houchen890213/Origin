package com.yulin.common.base.module;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yulin.common.R;
import com.yulin.common.annotation.Content;
import com.yulin.common.base.module.i.IActivityAnimation;
import com.yulin.common.base.i.ILifeCycle;
import com.yulin.common.base.page.Page;
import com.yulin.common.base.page.PageIntent;
import com.yulin.common.base.page.PageManager;
import com.yulin.common.base.page.i.OnPageViewListener;
import com.yulin.common.listener.IPresenter;
import com.yulin.common.logger.Logger;
import com.yulin.common.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by liu_lei on 2017/10/11.
 * 功能：1. 查找布局并加载， 2. 实现生命周期方法， 3. 实现presenter， 4. 实现一些公共方法， 5. 实现跳转动画，
 * 6. 禁止保存状态， 7. onKeyDown/onKeyUp处理， 8. 启动Activity，Fragment， 9， 将包含page的控件注册到module中，
 * 10. finishPage、回退、finish
 */

public class Module<T extends ViewDataBinding> extends RxAppCompatActivity implements IActivityAnimation, ILifeCycle, IPresenter {

    // 添加寻找layout时，向上遍历的终止位置
    private static final HashSet<Class<?>> END_POINT = new HashSet<>();

    // 静态代码块在类加载时执行
    static {
        END_POINT.add(Module.class);
    }

    protected Context context;
    protected T binding;
    protected View rootLayout;    // 根布局

    private PageManager mPageManager;

    private long lastKeyTime;    // 记录最后一次按键时间
    private long lastKeyTimeMain;     // 主页时最后一次按键时间
    // 在keyDown时设置为true，表示必须要有一次keyDown事件后，才能响应keyUp。两次按键时间间隔太短或已处理keyUp事件后，置为false。
    private boolean isCanKeyUp;
    private boolean isMainAct = false;
    private List<OnPageViewListener> mListPageViewListeners = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 进入动画, 第二个参数动画使用一个无效果的动画以防止切换时的黑屏问题
        overridePendingTransition(enterAnimation(), R.anim.anim_act_stay);

        context = this;

        beforeCreate(savedInstanceState);
        // 启动前，清除所有page栈
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        receiveData(getIntent());

        if (getLayoutId() > 0) {
            binding = DataBindingUtil.setContentView(this, getLayoutId());
            rootLayout = binding.getRoot();

            initView();
            initListener();
            initData();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiveNewData(intent);
    }

    protected void receiveNewData(Intent intent) {
    }

    protected void beforeCreate(Bundle savedInstanceState) {
    }

    protected void receiveData(Intent intent) {
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        long t = System.currentTimeMillis();

        PageIntent intent = getPageManager().getTopIntent();

        /*
        * 有page在回退栈的情况下，把事件交给page处理，如果page不处理，交给系统处理
        * */
        if (intent != null && intent.getTargetInstance() != null) {
            if (Math.abs(t - lastKeyTime) < 600) {
                isCanKeyUp = false;
                return true;
            }
            lastKeyTime = t;

            // 优先page自己处理keyUp
            if (isCanKeyUp && intent.getTargetInstance().onKeyUp(keyCode, event)) {
                isCanKeyUp = false;
                return true;
            } else {  //给系统处理(一般为关闭page,收起键盘等)
                isCanKeyUp = false;
                return super.onKeyUp(keyCode, event);
            }
        } else {
            // 无page 在回退栈的情况 先给pageSwitcher处理
            if (Math.abs(t - lastKeyTime) < 600) {
                isCanKeyUp = false;
                return true;
            }

            for (int i = 0; i < mListPageViewListeners.size(); i++) {
                if (mListPageViewListeners.get(i).onKeyUp(keyCode, event)) {
                    lastKeyTime = t;
                    isCanKeyUp = false;
                    return true;
                }
            }

            //无page 在回退栈的情况 且pageSwitcher不处理
            isCanKeyUp = false;
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (isMainAct) { //主页
                    if (t - lastKeyTimeMain < 1200) {
                        finish();   //结束主页,退出程序
                        return true;
                    } else {
                        lastKeyTimeMain = t;
                        toast("再按一次退出应用");
                        return true;
                    }
                } else {
                    // 其它activity
                    if (Math.abs(t - lastKeyTime) < 600) {
                        return true;
                    }
                    lastKeyTime = t;
                    return super.onKeyUp(keyCode, event);
                }
            }
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        isCanKeyUp = true;

        PageIntent intent = getPageManager().getTopIntent();
        if (intent != null && intent.getTargetInstance() != null) {
            if (intent.getTargetInstance().onKeyDown(keyCode, event)) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int enterAnimation() {
        return 0;
    }

    @Override
    public int exitAnimation() {
        return 0;
    }

    @Override
    public boolean isSupportAnimation() {
        return true;
    }

    @Override
    public void onViewClick(View view) {
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
    }

    private int getLayoutId() {
        try {
            Content contentLayout = getContentLayout(getClass());
            if (contentLayout != null) {
                return contentLayout.value();
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
     * 设置view是否显示
     * invisible时只隐藏不gone
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
     * 弹出toast
     */
    protected void toast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转activity
     */
    protected void next(Class<? extends Module> clz) {
        ActivityUtil.next(context, clz);
    }

    public void startActForResult(Class<? extends Module> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    public void startActForResult(Bundle bundle, Class<? extends Module> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void startAct(Class<? extends Module> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public void startAct(Bundle bundle, Class<? extends Module> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 启动新界面
     *
     * @param containerId page所在的视图id
     * @param intent      page意图
     */
    public void startPage(int containerId, PageIntent intent) {
        startPage(containerId, intent, -1);
    }

    /**
     * 启动新界面
     *
     * @param containerId page所在的视图id
     * @param intent      page意图
     */
    private void startPage(int containerId, PageIntent intent, int requestCode) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        int flag = intent.getFlags();
        Bundle bundle = intent.getArguments();
        Page targetPage = intent.getTargetInstance();

        if (targetPage == null) return;

        transaction.setCustomAnimations(targetPage.enterAnimation(), targetPage.exitAnimation(), targetPage.popEnterAnimation(), targetPage.popExitAnimation());
        targetPage.setArguments(bundle);
        targetPage.setPageIntent(intent);
        targetPage.setRequest(requestCode);

        // 子page中触发的界面切换请求，统一由父page来处理
        while (targetPage.mLinkedContext != null && targetPage.mLinkedContext instanceof Page) {
            Page linkedPage = (Page) targetPage.mLinkedContext;
            if (linkedPage.getParent() != null) {
                targetPage.mLinkedContext = linkedPage.getParent();
            } else {
                break;
            }
        }

        switch (flag) {
            case PageIntent.FLAG_PAGE_NO_HISTORY:
                targetPage.mContainerId = containerId;
                // 销毁被依赖的，且无需记录的page
                if (targetPage.mLinkedContext != null && targetPage.mLinkedContext.getPageIntent() != null) {
                    PageIntent p = targetPage.mLinkedContext.getPageIntent();
                    if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                        if (targetPage.mLinkedContext instanceof Fragment) {
                            transaction.remove((Fragment) targetPage.mLinkedContext);
                        }
                    }
                }
                transaction.add(containerId, targetPage);
                transaction.commitAllowingStateLoss();
                break;
            case PageIntent.FLAG_PAGE_REPLACE:
                targetPage.mContainerId = containerId;
                transaction.replace(containerId, targetPage);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commitAllowingStateLoss();
                break;
            case PageIntent.FLAG_PAGE_REPLACE_AND_NO_HISTORY:
                targetPage.mContainerId = containerId;
                if (targetPage.mLinkedContext != null && targetPage.mLinkedContext.getPageIntent() != null) {
                    PageIntent p = targetPage.mLinkedContext.getPageIntent();
                    if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                        if (targetPage.mLinkedContext instanceof Fragment) {
                            transaction.remove((Fragment) targetPage.mLinkedContext);
                        }
                    }
                }
                transaction.replace(containerId, targetPage);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commitAllowingStateLoss();
                break;
            case PageIntent.FLAG_PAGE_CLEAR_TOP:
                targetPage.mContainerId = containerId;
                //返回true:找到page并清掉上层成功;返回false:添加page到顶层
                if (!getPageManager().popPageTopOf(intent)) {
                    if (getPageManager().hasIntent(intent)) {
                        getPageManager().getTopIntent().getTargetInstance().dispatchNewIntent(intent);
                    } else {
                        if (targetPage.mLinkedContext != null && targetPage.mLinkedContext.getPageIntent() != null) {
                            PageIntent p = targetPage.mLinkedContext.getPageIntent();
                            if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                                if (targetPage.mLinkedContext instanceof Fragment) {
                                    transaction.remove((Fragment) targetPage.mLinkedContext);
                                }
                            }
                        }
                        transaction.add(containerId, targetPage);
                        transaction.addToBackStack(targetPage.getStackKey());
                        transaction.commitAllowingStateLoss();
                    }
                }
                break;
            case PageIntent.FLAG_PAGE_STANDARD:
            default:
                targetPage.mContainerId = containerId;
                if (targetPage.mLinkedContext != null && targetPage.mLinkedContext.getPageIntent() != null) {
                    PageIntent p = targetPage.mLinkedContext.getPageIntent();
                    if (p.getFlags() == PageIntent.FLAG_PAGE_NO_HISTORY) {
                        if (targetPage.mLinkedContext instanceof Fragment) {
                            transaction.remove((Fragment) targetPage.mLinkedContext);
                        }
                    }
                }
                transaction.add(containerId, targetPage);
                transaction.addToBackStack(targetPage.getStackKey());
                transaction.commitAllowingStateLoss();
                break;
        }
    }

    /**
     * 启动新界面
     *
     * @param containerId page所在的视图id
     * @param intent      page意图
     * @param requestCode 请求的code
     */
    public void startPageForResult(int containerId, PageIntent intent, int requestCode) {
        startPage(containerId, intent, requestCode);
    }

    /**
     * 获取page管理器
     */
    public PageManager getPageManager() {
        if (mPageManager == null) {
            mPageManager = new PageManager(getSupportFragmentManager());
        }
        return mPageManager;
    }

    public void setMainAct(boolean mainAct) {
        isMainAct = mainAct;
    }

    /**
     * 将包含page的控件注册到该activity，以此管理view中包含的page
     */
    public void registerViewWithPage(OnPageViewListener listener) {
        if (listener == null) return;

        if (!mListPageViewListeners.contains(listener)) {
            listener.registToAct(this);
            mListPageViewListeners.add(listener);
        }
    }

    public View getContentView() {
        return rootLayout;
    }

    public void finishToPage(PageIntent intent) {
        if (!getPageManager().popPageTopOf(intent)) {
            if (getPageManager().hasIntent(intent)) {
                getPageManager().getTopIntent().getTargetInstance().dispatchNewIntent(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, exitAnimation());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, exitAnimation());
    }

}


