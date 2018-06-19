package com.yulin.common.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by liu_lei on 2017/5/24.
 * <p>
 * data_binding模式下使用的ViewHolder，持有viewDataBinding对象
 */

public class DbViewHolder extends BaseViewHolder {

    private ViewDataBinding mBinding;

    DbViewHolder(View view) {
        super(view);
        mBinding = DataBindingUtil.bind(itemView);
    }

    ViewDataBinding getBinding() {
        return mBinding;
    }

}
