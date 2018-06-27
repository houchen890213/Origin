package com.yulin.origin.business.recent.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yulin.common.adapter.pure.OnItemClickListener;
import com.yulin.common.adapter.pure.PureAdapter;
import com.yulin.common.annotation.Content;
import com.yulin.common.logger.Logger;
import com.yulin.frame.base.ui.PageImpl;
import com.yulin.io.retrofit.observer.BaseObserver;
import com.yulin.io.retrofit.response.RequestRet;
import com.yulin.origin.BR;
import com.yulin.origin.R;
import com.yulin.origin.business.content.ui.PoemContentActivity;
import com.yulin.origin.business.recent.bean.PoemItemBean;
import com.yulin.origin.business.recent.vm.RecentPoemsVm;
import com.yulin.origin.databinding.PageRecentPoemsBinding;

import io.reactivex.annotations.NonNull;

@Content(R.layout.page_recent_poems)
public class RecentPoemsPage extends PageImpl<PageRecentPoemsBinding> {

    private RecentPoemsVm mVm;
    private PureAdapter<PoemItemBean> mAdapter;

    @Override
    public void initView() {
        super.initView();
        setTitle("首页");
        removeNavigationIcon();

        mVm = new RecentPoemsVm();
        binding.setVm(mVm);

        binding.contentView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new PureAdapter<>(R.layout.item_poems, mVm.getItems(), BR.bean);
        binding.contentView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(PureAdapter adapter, View view, int position) {
                next(PoemContentActivity.class);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        mVm.requestPoems(new BaseObserver<RequestRet>() {
            @Override
            public void onComplete() {
                refreshEnd();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                refreshEnd();
                Logger.e(e.getMessage());
            }
        });
    }

}
