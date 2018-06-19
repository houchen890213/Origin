package com.yulin.origin.business.category.bean;

import android.databinding.Bindable;

import com.chad.library.adapter.base.entity.IExpandable;
import com.yulin.origin.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu_lei on 2017/7/10.
 */

public class CategorySectionBean extends CategoryBean implements IExpandable<CategoryItemBean> {

    private String name;
    private boolean isExpanded;
    private List<CategoryItemBean> subItems = new ArrayList<>();

    public CategorySectionBean(String name) {
        super(VIEW_TYPE_SECTION);
        setName(name);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Override
    public String toString() {
        return "CategorySectionBean{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public List<CategoryItemBean> getSubItems() {
        return subItems;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    public void addSubItem(CategoryItemBean item) {
        subItems.add(item);
    }

}
