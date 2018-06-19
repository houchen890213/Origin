package com.yulin.origin.business.category.adapter;

import android.support.annotation.Nullable;

import com.yulin.common.adapter.DbBaseMultiItemAdapter;
import com.yulin.common.adapter.pure.PureMultiAdapter;
import com.yulin.origin.BR;
import com.yulin.origin.R;
import com.yulin.origin.business.category.bean.CategoryBean;

import java.util.List;

/**
 * Created by liu_lei on 2017/7/10.
 */

public class CategoryAdapter extends PureMultiAdapter<CategoryBean> {

    public CategoryAdapter(@Nullable List<CategoryBean> data) {
        super(data);

        addItemType(CategoryBean.VIEW_TYPE_SECTION, R.layout.item_category_section, BR.bean);
        addItemType(CategoryBean.VIEW_TYPE_NORMAL, R.layout.item_category_normal, BR.bean);
    }

}
