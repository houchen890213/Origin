package com.yulin.origin.business.content.ui;

import com.yulin.common.annotation.Content;
import com.yulin.common.logger.Logger;
import com.yulin.frame.base.ui.ModuleImpl;
import com.yulin.io.retrofit.observer.BaseObserver;
import com.yulin.io.retrofit.response.RequestRet;
import com.yulin.origin.R;
import com.yulin.origin.business.content.vm.PoemContentVm;
import com.yulin.origin.databinding.ActivityPoemContentBinding;

import io.reactivex.annotations.NonNull;

/**
 * Created by liu_lei on 2017/6/29.
 * 诗词详情
 */

@Content(R.layout.activity_poem_content)
public class PoemContentActivity extends ModuleImpl<ActivityPoemContentBinding> {

    private PoemContentVm mVm;

    @Override
    public void initView() {
        super.initView();
        setTitle("诗词详情");

        mVm = new PoemContentVm();
        binding.setVm(mVm);
    }

    @Override
    public void initData() {
        super.initData();

        mVm.requestPoemContent(1, new BaseObserver<RequestRet>() {
            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
                Logger.e(e.getMessage());
            }

            @Override
            public void onComplete() {
                super.onComplete();
                setTitle(mVm.getTitle());
            }
        });
    }

}
