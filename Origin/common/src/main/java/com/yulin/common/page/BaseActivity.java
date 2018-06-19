package com.yulin.common.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by liu_lei on 2017/10/12.
 * 
 */

public class BaseActivity extends RxAppCompatActivity {

    private PageManager mPageManager;

    public void startActForResult(Class<? extends BaseActivity> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    public void startActForResult(Bundle bundle, Class<? extends BaseActivity> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void startAct(Class<? extends BaseActivity> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public void startAct(Bundle bundle, Class<? extends BaseActivity> clz) {
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
//                transaction.addToBackStack(targetPage.getStackKey());
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

    public void finishToPage(PageIntent intent) {
        if (!getPageManager().popPageTopOf(intent)) {
            if (getPageManager().hasIntent(intent)) {
                getPageManager().getTopIntent().getTargetInstance().dispatchNewIntent(intent);
            }
        }
    }
    
}
