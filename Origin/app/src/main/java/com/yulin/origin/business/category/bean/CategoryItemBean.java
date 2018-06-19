package com.yulin.origin.business.category.bean;

import android.databinding.Bindable;

import com.yulin.origin.BR;

/**
 * Created by liu_lei on 2017/7/10.
 */

public class CategoryItemBean extends CategoryBean {

    private int id;
    private String name;
    private String cover;

    public CategoryItemBean(int id, String name, String url) {
        super(VIEW_TYPE_NORMAL);
        setId(id);
        setName(name);
        setCover(url);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getCover() {
        return cover;
    }

    public void setCover(String iconUrl) {
        this.cover = iconUrl;
        notifyPropertyChanged(BR.cover);
    }

    @Override
    public String toString() {
        return "CategoryItemBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

}
