package com.yulin.origin.helper;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.yulin.common.helper.ImageHelper;
import com.yulin.common.logger.Logger;
import com.yulin.origin.business.category.bean.CategoryBean;
import com.yulin.origin.business.recent.bean.PoemItemBean;

import java.util.List;

/**
 * Created by liu_lei on 2017/5/18.
 * <p>
 * 集中存放data_binding的adapter
 */

public class BindingAdapterCenter {

    @BindingAdapter("android:src")
    public static void src(ImageView view, String url) {
        ImageHelper.display(view, url);
    }

    // 首页 最近阅读
    @BindingAdapter({"items"})
    public static void recentItems(RecyclerView recyclerView, List<PoemItemBean> items) {
        if (items != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    // 首页 分类列表
    @BindingAdapter({"items"})
    public static void categoryItems(RecyclerView recyclerView, List<CategoryBean> items) {
        if (items != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

}
