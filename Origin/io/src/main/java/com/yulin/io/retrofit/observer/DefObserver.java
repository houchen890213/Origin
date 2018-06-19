package com.yulin.io.retrofit.observer;

import com.yulin.io.retrofit.exception.NetworkException;
import com.yulin.io.retrofit.listener.OnApiListener;
import com.yulin.io.retrofit.response.DefResponse;

import io.reactivex.disposables.Disposable;

/**
 * Created by yaowang on 2017/5/3.
 */

public class DefObserver<T> extends BaseObserver<DefResponse<T>> {

    protected OnApiListener<T> listener;

    public DefObserver(OnApiListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(DefResponse<T> response) {
        if (response.getRetCode() == DefResponse.SUCCESS_CODE ) {
            T t = response.getData();
            if (listener != null) {
                listener.onSuccess(t);
            }
        } else {
            onError(new NetworkException(response.getRetCode(), response.getRetMsg()));
        }
    }

    @Override
    public void onError(Throwable e) {
        if (listener != null) {
            listener.onError(e);
        }
    }

    @Override
    public void onComplete() {

    }
}
