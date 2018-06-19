package com.yulin.origin.business.category.bean;

import com.yulin.common.adapter.MultiItemBean;

/**
 * Created by liu_lei on 2017/7/10.
 */

public class CategoryBean extends MultiItemBean {

    public static final int VIEW_TYPE_SECTION = 1;
    public static final int VIEW_TYPE_NORMAL = 2;

    CategoryBean(int viewType) {
        super(viewType);
    }

}
