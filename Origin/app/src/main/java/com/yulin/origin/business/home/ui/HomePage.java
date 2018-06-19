package com.yulin.origin.business.home.ui;

import com.yulin.common.annotation.Content;
import com.yulin.frame.base.ui.PageImpl;
import com.yulin.origin.R;
import com.yulin.origin.databinding.PageMainBinding;

/**
 * Created by liu_lei on 2017/7/2.
 * 首页
 */

@Content(R.layout.page_main)
public class HomePage extends PageImpl<PageMainBinding> {

    @Override
    public void initView() {
        super.initView();
        setTitle("首页");

        removeNavigationIcon();
    }

}
