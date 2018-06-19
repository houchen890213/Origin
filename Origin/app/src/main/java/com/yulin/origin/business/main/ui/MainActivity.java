package com.yulin.origin.business.main.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.yulin.common.annotation.Content;
import com.yulin.common.widget.TabItem;
import com.yulin.frame.base.ui.ModuleImpl;
import com.yulin.origin.R;
import com.yulin.origin.business.category.ui.CategoryPage;
import com.yulin.origin.business.discovery.ui.DiscoveryPage;
import com.yulin.origin.business.mine.ui.MinePage;
import com.yulin.origin.business.recent.ui.RecentPoemsPage;
import com.yulin.origin.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu_lei on 2017/7/2.
 * <p>
 * 主页（首页、分类、发现、我的）
 */

@Content(R.layout.activity_main)
public class MainActivity extends ModuleImpl<ActivityMainBinding> {

    private List<Fragment> mPages = new ArrayList<>();
    private List<TabItem> mTabs = new ArrayList<>();

    private long exitNow;

    @Override
    public void initView() {
        super.initView();
        initStatusBar();    // 全屏显示

        binding.setPresenter(this);

        mPages.add(new RecentPoemsPage());
        mPages.add(new CategoryPage());
        mPages.add(new DiscoveryPage());
        mPages.add(new MinePage());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mPages.get(position);
            }

            @Override
            public int getCount() {
                return mPages.size();
            }
        };

        binding.viewPager.setOffscreenPageLimit(3);    // 左右保持3个页面不销毁，否则，页面切换时，会频繁销毁重建page
        binding.viewPager.setAdapter(adapter);

        mTabs.add(binding.tabMain);
        mTabs.add(binding.tabCategory);
        mTabs.add(binding.tabDiscovery);
        mTabs.add(binding.tabMine);

        resetOtherTabs();
        binding.tabMain.setState(TabItem.STATE_SELECT);
    }

    @Override
    public void onViewClick(View view) {
        super.onViewClick(view);
        resetOtherTabs();

        switch (view.getId()) {
            case R.id.tab_main:
                mTabs.get(0).setState(TabItem.STATE_SELECT);
                binding.viewPager.setCurrentItem(0, false);
                break;
            case R.id.tab_category:
                mTabs.get(1).setState(TabItem.STATE_SELECT);
                binding.viewPager.setCurrentItem(1, false);
                break;
            case R.id.tab_discovery:
                mTabs.get(2).setState(TabItem.STATE_SELECT);
                binding.viewPager.setCurrentItem(2, false);
                break;
            case R.id.tab_mine:
                mTabs.get(3).setState(TabItem.STATE_SELECT);
                binding.viewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            if ((System.currentTimeMillis() - exitNow) > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitNow = System.currentTimeMillis();
            } else if ((System.currentTimeMillis() - exitNow) > 0) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabs.size(); i++) {
            mTabs.get(i).setState(TabItem.STATE_NORMAL);
        }
    }

}
