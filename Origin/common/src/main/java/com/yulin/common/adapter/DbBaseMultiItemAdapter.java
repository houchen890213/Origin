package com.yulin.common.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;

import java.util.List;

/**
 * Created by liu_lei on 2017/5/24.
 */

public class DbBaseMultiItemAdapter<T extends MultiItemBean> extends DbBaseAdapter<T> {

    private SparseIntArray layouts;
    private SparseIntArray variableIds;

    public DbBaseMultiItemAdapter(@Nullable List<T> data) {
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
        return layouts.get(viewType);
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

    @Override
    protected final int getVariableId(int viewType) {
        if (variableIds != null)
            return variableIds.get(viewType);

        return 0;
    }

}
