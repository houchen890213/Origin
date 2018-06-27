package com.yulin.origin.business.recent.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yulin.common.adapter.DbBaseAdapter;
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
    private DbBaseAdapter<PoemItemBean> adapter;

    @Override
    public void initView() {
        super.initView();
        setTitle("首页");
        removeNavigationIcon();

        mVm = new RecentPoemsVm();
        binding.setVm(mVm);

        binding.contentView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DbBaseAdapter<>(R.layout.item_poems, mVm.getItems(), BR.bean);
        binding.contentView.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        super.initListener();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                next(PoemContentActivity.class);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        mVm.requestPoems(new BaseObserver<RequestRet>() {
            @Override
            public void onNext(@NonNull RequestRet requestRet) {
                super.onNext(requestRet);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                refreshEnd();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
                refreshEnd();

                Logger.e(e.getMessage());
            }
        });
    }

}
