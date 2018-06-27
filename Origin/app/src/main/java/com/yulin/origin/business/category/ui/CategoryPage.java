package com.yulin.origin.business.category.ui;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.yulin.common.adapter.pure.OnItemClickListener;
import com.yulin.common.adapter.pure.PureAdapter;
import com.yulin.common.annotation.Content;
import com.yulin.common.logger.Logger;
import com.yulin.frame.base.ui.PageImpl;
import com.yulin.io.retrofit.observer.BaseObserver;
import com.yulin.io.retrofit.response.RequestRet;
import com.yulin.origin.R;
import com.yulin.origin.business.category.adapter.CategoryAdapter;
import com.yulin.origin.business.category.bean.CategoryBean;
import com.yulin.origin.business.category.bean.CategoryItemBean;
import com.yulin.origin.business.category.bean.CategorySectionBean;
import com.yulin.origin.business.category.vm.CategoryVm;
import com.yulin.origin.databinding.PageCategoryBinding;

import io.reactivex.annotations.NonNull;

/**
 * Created by liu_lei on 2017/7/2.
 * 分类
 */

@Content(R.layout.page_category)
public class CategoryPage extends PageImpl<PageCategoryBinding> {

    private CategoryVm mVm;

    private CategoryAdapter mAdapter;

    @Override
    public void initView() {
        super.initView();
        setTitle("分类");

        removeNavigationIcon();

        mVm = new CategoryVm();
        binding.setVm(mVm);

        GridLayoutManager manager = new GridLayoutManager(context, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                CategoryBean bean = mVm.getItem(position);
                if (bean != null && bean instanceof CategorySectionBean) {
                    return 4;
                }

                return 1;
            }

        });
        binding.contentView.setLayoutManager(manager);
        mAdapter = new CategoryAdapter(mVm.getItems());
        binding.contentView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(PureAdapter adapter, View view, int position) {
                CategoryBean item = mVm.getItem(position);
                if (item instanceof CategoryItemBean) {
                    CategoryItemBean bean = (CategoryItemBean) item;
                    Logger.d("item " + bean.getName() + " " + position);
                } else if (item instanceof CategorySectionBean) {
                    CategorySectionBean section = (CategorySectionBean) item;
                    Logger.d("section " + section.getName() + " " + position);
                    if (section.isExpanded()) {
                        mAdapter.collapse(position);
                    } else {
                        mAdapter.expand(position);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mVm.requestCategory(new BaseObserver<RequestRet>() {
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
