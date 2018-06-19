package com.yulin.common.adapter.pure;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.entity.IExpandable;
import com.yulin.common.logger.Logger;

import java.util.List;

/**
 * Created by liu_lei on 2017/7/12.
 * 直接继承RecyclerView.Adapter
 */

public class PureAdapter<T> extends RecyclerView.Adapter<PureViewHolder> {

    List<T> mData;

    private int mLayoutResId;
    private int mVariableId;

    private OnItemClickListener mClickListener;

    public PureAdapter(@LayoutRes int layoutResId, @Nullable List<T> data, int variableId) {
        mLayoutResId = layoutResId;
        mData = data;
        mVariableId = variableId;
    }

    @Override
    public PureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PureViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(final PureViewHolder holder, int position) {
        holder.getBinding().setVariable(getVariableId(getItemViewType(position)), mData.get(position));
        holder.getBinding().executePendingBindings();

        final int index = holder.getLayoutPosition();
        Logger.d("adapter position: " + holder.getAdapterPosition() + ", position: " + position);
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(PureAdapter.this, holder.getBinding().getRoot(), index);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    public int collapse(@IntRange(from = 0) int position) {
        return collapse(position, true, true);
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    public int collapse(@IntRange(from = 0) int position, boolean animate) {
        return collapse(position, animate, true);
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @param animate  collapse with animation or not.
     * @param notify   notify the recyclerView refresh UI or not.
     * @return the number of subItems collapsed.
     */
    public int collapse(@IntRange(from = 0) int position, boolean animate, boolean notify) {
        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        int subItemCount = recursiveCollapse(position);
        expandable.setExpanded(false);
        if (notify) {
//            if (animate) {
//                notifyItemChanged(position);
//                notifyItemRangeRemoved(position + 1, subItemCount);
//            } else {
                notifyDataSetChanged();
//            }
        }
        return subItemCount;
    }

    /**
     * Expand an expandable item
     *
     * @param position position of the item, which includes the header layout count.
     * @param animate  expand items with animation
     * @return the number of items that have been added.
     */
    public int expand(@IntRange(from = 0) int position, boolean animate) {
        return expand(position, animate, true);
    }

    /**
     * Expand an expandable item with animation.
     *
     * @param position position of the item, which includes the header layout count.
     * @return the number of items that have been added.
     */
    public int expand(@IntRange(from = 0) int position) {
        return expand(position, true, true);
    }

    /**
     * Expand an expandable item
     *
     * @param position     position of the item
     * @param animate      expand items with animation
     * @param shouldNotify notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it
     *                     yourself.
     * @return the number of items that have been added.
     */
    @SuppressWarnings("unchecked")
    public int expand(@IntRange(from = 0) int position, boolean animate, boolean shouldNotify) {
        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        if (!hasSubItems(expandable)) {
            expandable.setExpanded(false);
            return 0;
        }
        int subItemCount = 0;
        if (!expandable.isExpanded()) {
            List list = expandable.getSubItems();
            mData.addAll(position + 1, list);
            subItemCount += recursiveExpand(position + 1, list);

            expandable.setExpanded(true);
            subItemCount += list.size();
        }
        if (shouldNotify) {
//            if (animate) {
//                notifyItemChanged(position);
//                notifyItemRangeInserted(position + 1, subItemCount);
//            } else {
                notifyDataSetChanged();
//            }
        }
        return subItemCount;
    }

    public int expandAll(int position, boolean animate, boolean notify) {

        T endItem = null;
        if (position + 1 < this.mData.size()) {
            endItem = getItem(position + 1);
        }

        IExpandable expandable = getExpandableItem(position);
        if (expandable == null || !hasSubItems(expandable)) {
            return 0;
        }

        int count = expand(position, false, false);
        for (int i = position + 1; i < this.mData.size(); i++) {
            T item = getItem(i);

            if (item == endItem) {
                break;
            }
            if (isExpandable(item)) {
                count += expand(i, false, false);
            }
        }

        if (notify) {
            if (animate) {
                notifyItemRangeInserted(position + 1, count);
            } else {
                notifyDataSetChanged();
            }
        }
        return count;
    }

    /**
     * expand the item and all its subItems
     *
     * @param position position of the item, which includes the header layout count.
     * @param init     whether you are initializing the recyclerView or not.
     *                 if <strong>true</strong>, it won't notify recyclerView to redraw UI.
     * @return the number of items that have been added to the adapter.
     */
    public int expandAll(int position, boolean init) {
        return expandAll(position, true, !init);
    }

    public void expandAll() {
        for (int i = mData.size() - 1; i >= 0; i--) {
            expandAll(i, false, false);
        }
    }

    @SuppressWarnings("unchecked")
    private int recursiveCollapse(@IntRange(from = 0) int position) {
        T item = getItem(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        int subItemCount = 0;
        if (expandable != null && expandable.isExpanded()) {
            List<T> subItems = expandable.getSubItems();
            for (int i = subItems.size() - 1; i >= 0; i--) {
                T subItem = subItems.get(i);
                int pos = getItemPosition(subItem);
                if (pos < 0) {
                    continue;
                }
                if (subItem instanceof IExpandable) {
                    subItemCount += recursiveCollapse(pos);
                }
                mData.remove(pos);
                subItemCount++;
            }
        }
        return subItemCount;
    }

    @SuppressWarnings("unchecked")
    private int recursiveExpand(int position, @NonNull List list) {
        int count = 0;
        int pos = position + list.size() - 1;
        for (int i = list.size() - 1; i >= 0; i--, pos--) {
            if (list.get(i) instanceof IExpandable) {
                IExpandable item = (IExpandable) list.get(i);
                if (item.isExpanded() && hasSubItems(item)) {
                    List subList = item.getSubItems();
                    mData.addAll(pos + 1, subList);
                    int subItemCount = recursiveExpand(pos + 1, subList);
                    count += subItemCount;
                }
            }
        }
        return count;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Nullable
    public T getItem(@IntRange(from = 0) int position) {
        if (position < mData.size())
            return mData.get(position);
        else
            return null;
    }

    private IExpandable getExpandableItem(int position) {
        T item = getItem(position);
        if (isExpandable(item)) {
            return (IExpandable) item;
        } else {
            return null;
        }
    }

    public boolean isExpandable(T item) {
        return item != null && item instanceof IExpandable;
    }

    private boolean hasSubItems(IExpandable item) {
        if (item == null) {
            return false;
        }
        List list = item.getSubItems();
        return list != null && list.size() > 0;
    }

    private int getItemPosition(T item) {
        return item != null && mData != null && !mData.isEmpty() ? mData.indexOf(item) : -1;
    }

}
