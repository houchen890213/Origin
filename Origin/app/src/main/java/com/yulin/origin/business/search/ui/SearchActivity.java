package com.yulin.origin.business.search.ui;

import android.view.View;

import com.yulin.common.annotation.Content;
import com.yulin.frame.base.ui.ModuleImpl;
import com.yulin.origin.R;
import com.yulin.origin.databinding.ActivitySearchBinding;

/**
 * Created by liu_lei on 2017/7/3.
 *
 */

@Content(R.layout.activity_search)
public class SearchActivity extends ModuleImpl<ActivitySearchBinding> {

    @Override
    public void initView() {
        super.initView();
        initStatusBar();

        binding.setPresenter(this);
    }

    @Override
    public void onViewClick(View view) {
        super.onViewClick(view);

        switch (view.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
                break;
        }
    }

}
