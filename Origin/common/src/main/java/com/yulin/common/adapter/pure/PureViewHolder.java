package com.yulin.common.adapter.pure;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liu_lei on 2017/7/12.
 * 直接继承Recycler.ViewHolder
 */

class PureViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mBinding;

    PureViewHolder(View view) {
        super(view);
        mBinding = DataBindingUtil.bind(itemView);
    }

    ViewDataBinding getBinding() {
        return mBinding;
    }

}
