package com.yulin.origin.business.category.vm;

import android.databinding.Bindable;

import com.yulin.common.logger.Logger;
import com.yulin.frame.base.vm.BaseVm;
import com.yulin.io.retrofit.observer.BaseObserver;
import com.yulin.io.retrofit.response.RequestRet;
import com.yulin.origin.BR;
import com.yulin.origin.business.category.bean.CategoryBean;
import com.yulin.origin.business.category.bean.CategoryItemBean;
import com.yulin.origin.business.category.bean.CategorySectionBean;
import com.yulin.origin.business.category.response.Book;
import com.yulin.origin.business.category.response.CategoryResponse;
import com.yulin.origin.business.category.response.Item;
import com.yulin.origin.network.service.PoemService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by liu_lei on 2017/7/2.
 *
 */

public class CategoryVm extends BaseVm {

    private List<CategoryBean> items = new ArrayList<>();

    public void requestCategory(BaseObserver<RequestRet> observer) {
//        String token = getUserInfo().getToken();

        getService(PoemService.class).getCategory()
                .map(new Function<CategoryResponse, RequestRet>() {
                    @Override
                    public RequestRet apply(@NonNull CategoryResponse response) throws Exception {
                        Logger.d(response.toString());
                        updateItems(response);
                        return new RequestRet(1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void updateItems(CategoryResponse response) {
        if (response == null) return;

        items.clear();

        List<Item> categories = response.getItems();
        for (Item category : categories) {
            CategorySectionBean section = new CategorySectionBean(category.getName());
            this.items.add(section);

            List<Book> books = category.getBooks();
            if (books != null && books.size() > 0) {
                for (Book book : books) {
                    CategoryItemBean item = new CategoryItemBean(book.getId(), book.getName(), book.getCover());
                    this.items.add(item);
                    section.addSubItem(item);
                }
                section.setExpanded(true);
            }
        }

        setItems(this.items);
    }

    public CategoryBean getItem(int position) {
        if (items != null && items.size() > position)
            return items.get(position);

        return null;
    }

    @Bindable
    public List<CategoryBean> getItems() {
        return items;
    }

    public void setItems(List<CategoryBean> items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }

}
