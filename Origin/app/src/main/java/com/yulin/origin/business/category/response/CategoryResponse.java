package com.yulin.origin.business.category.response;

import java.util.List;

/**
 * Created by liu_lei on 2017/7/10.
 */

public class CategoryResponse {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "items=" + items +
                '}';
    }

}
