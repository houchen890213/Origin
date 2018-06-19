package com.yulin.origin.business.recent.vm;

import android.databinding.Bindable;

import com.yulin.frame.base.vm.BaseVm;
import com.yulin.io.retrofit.observer.BaseObserver;
import com.yulin.io.retrofit.response.RequestRet;
import com.yulin.origin.BR;
import com.yulin.origin.business.recent.bean.PoemItemBean;
import com.yulin.origin.business.recent.response.PoemItemResponse;
import com.yulin.origin.business.recent.response.PoemListResponse;
import com.yulin.origin.network.service.PoemService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by liu_lei on 2017/5/29.
 *
 */

public class RecentPoemsVm extends BaseVm {

    private List<PoemItemBean> items = new ArrayList<>();

    public void requestPoems(BaseObserver<RequestRet> observer) {
        getService(PoemService.class).getPoems()
                .map(new Function<PoemListResponse, RequestRet>() {
                    @Override
                    public RequestRet apply(@NonNull PoemListResponse poemListResponse) throws Exception {
                        updateList(poemListResponse);
                        return new RequestRet(1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void updateList(PoemListResponse response) {
        if (response != null) {
            items.clear();

            List<PoemItemResponse> poems = response.getPoems();
            for (PoemItemResponse poem : poems) {
                PoemItemBean item = new PoemItemBean();

                item.setTitle(poem.getTitle());
                item.setAuthor(poem.getAuthor());
                item.setDynasty(poem.getDynasty());
                item.setLastRead(poem.getLastRead());
                item.setPoemId(poem.getPoemId());

                items.add(item);
            }

            setItems(items);
        }
    }

    @Bindable
    public List<PoemItemBean> getItems() {
        return items;
    }

    public void setItems(List<PoemItemBean> items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }

}
