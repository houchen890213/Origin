package com.yulin.origin.business.discovery.ui;

import android.view.View;

import com.yulin.common.annotation.Content;
import com.yulin.frame.base.ui.PageImpl;
import com.yulin.origin.R;
import com.yulin.origin.business.search.ui.SearchActivity;
import com.yulin.origin.databinding.PageDiscoveryBinding;

/**
 * Created by liu_lei on 2017/7/2.
 * 发现
 */

@Content(R.layout.page_discovery)
public class DiscoveryPage extends PageImpl<PageDiscoveryBinding> {

    @Override
    public void initView() {
        super.initView();
        setTitle("发现");

        binding.setPresenter(this);

        setNavigationIcon(R.drawable.ic_search_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next(SearchActivity.class);
            }
        });
    }

}
