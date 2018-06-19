package com.yulin.common.adapter.pure;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;

import com.yulin.common.adapter.MultiItemBean;

import java.util.List;

/**
 * Created by liu_lei on 2017/7/12.
 * 多布局结构
 */

public class PureMultiAdapter<T extends MultiItemBean> extends PureAdapter<T> {

    private SparseIntArray layouts;
    private SparseIntArray variableIds;

    public PureMultiAdapter(@Nullable List<T> data) {
        super(0, data, 0);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId, int variableId) {
        if (layouts == null)
            layouts = new SparseIntArray();
        layouts.put(type, layoutResId);

        if (variableIds == null)
            variableIds = new SparseIntArray();
        variableIds.put(type, variableId);
    }

    @Override
    protected final int getItemLayoutId(int viewType) {
        if (layouts != null)
            return layouts.get(viewType);

        return 0;
    }

    @Override
    protected final int getVariableId(int viewType) {
        if (variableIds != null)
            return variableIds.get(viewType);

        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        if (mData != null && mData.size() > position) {
            T item = mData.get(position);

            if (item != null)
                return item.getItemViewType();
        }

        return super.getItemViewType(position);
    }

}
