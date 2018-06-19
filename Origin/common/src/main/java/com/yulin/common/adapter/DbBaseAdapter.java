package com.yulin.common.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yulin.common.BR;

import java.util.List;

/**
 * Created by liu_lei on 2017/5/24.
 * <p>
 * data_binding模式下adapter基类
 */

public class DbBaseAdapter<T> extends BaseQuickAdapter<T, DbViewHolder> {

    private int mVariableId;

    public DbBaseAdapter(@LayoutRes int layoutResId, @Nullable List<T> data, int variableId) {
        super(layoutResId, data);
        mLayoutResId = layoutResId;
        mVariableId = variableId;
    }

    @Override
    protected DbViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false);
        return new DbViewHolder(view);
    }

    @Override
    protected void convert(DbViewHolder helper, T item) {
        int position = helper.getLayoutPosition();
        helper.getBinding().setVariable(getVariableId(getItemViewType(position)), item);
        helper.getBinding().executePendingBindings();
    }

    /**
     * 根据viewType返回对应的item的layoutId
     * 支持多类型item
     *
     * @return layout_id
     */
    protected int getItemLayoutId(int viewType) {
        return mLayoutResId;
    }

    /**
     * 根据viewType返回对应的layout的variable的id
     * 支持多类型item
     *
     * @return BR id
     */
    protected int getVariableId(int viewType) {
        return mVariableId;
    }

}
