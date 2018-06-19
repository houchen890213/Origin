package com.yulin.origin.business.category.response;

import java.util.List;

/**
 * Created by liu_lei on 2017/9/5.
 */

public class Item {

    private String name;
    private List<Book> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
                "name='" + name + '\'' +
                ", books=" + books +
                '}';
    }

}
