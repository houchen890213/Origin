package com.yulin.origin.business.mine.ui;

import com.yulin.common.annotation.Content;
import com.yulin.frame.base.ui.PageImpl;
import com.yulin.origin.R;
import com.yulin.origin.business.mine.vm.MineVm;
import com.yulin.origin.databinding.PageMineBinding;

/**
 * Created by liu_lei on 2017/7/2.
 * 我的
 */

@Content(R.layout.page_mine)
public class MinePage extends PageImpl<PageMineBinding> {

    @Override
    public void initView() {
        super.initView();
        setTitle("我的");

        removeNavigationIcon();

        binding.setVm(new MineVm());
    }

}
