package com.yulin.origin.business.content.vm;

import android.databinding.Bindable;

import com.yulin.frame.base.vm.BaseVm;
import com.yulin.io.retrofit.observer.BaseObserver;
import com.yulin.io.retrofit.response.RequestRet;
import com.yulin.origin.BR;
import com.yulin.origin.business.content.response.PoemContentResponse;
import com.yulin.origin.network.service.PoemService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by liu_lei on 2017/6/29.
 */

public class PoemContentVm extends BaseVm {

    private String title;
    private String author;
    private String dynasty;
    private String body;       // 正文
    private String genre;
    private String motif;
    private String introduce;       // 简介
    private String annotation;      // 注释
    private String translate;       // 翻译
    private String background;      // 背景
    private String appreciate;      // 赏析

    // 获取诗词详情
    public void requestPoemContent(long poemId, BaseObserver<RequestRet> observer) {
        getService(PoemService.class).getPoemContent()
                .map(new Function<PoemContentResponse, RequestRet>() {
                    @Override
                    public RequestRet apply(@NonNull PoemContentResponse response) throws Exception {
                        updateContent(response);

                        return new RequestRet(1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void updateContent(PoemContentResponse response) {
        setTitle(response.getTitle());
        setBody(response.getBody());
        setAuthor(response.getAuthor());
        setDynasty(response.getDynasty());
        setGenre(response.getGenre());
        setMotif(response.getMotif());
        setIntroduce(response.getIntro());
        setAnnotation(response.getAnnotation());
        setTranslate(response.getTranslate());
        setBackground(response.getBackground());
        setAppreciate(response.getAppreciation());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        notifyPropertyChanged(BR.author);
    }

    @Bindable
    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
        notifyPropertyChanged(BR.dynasty);
    }

    @Bindable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        notifyPropertyChanged(BR.body);
    }

    @Bindable
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
        notifyPropertyChanged(BR.genre);
    }

    @Bindable
    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
        notifyPropertyChanged(BR.motif);
    }

    @Bindable
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
        notifyPropertyChanged(BR.introduce);
    }

    @Bindable
    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
        notifyPropertyChanged(BR.annotation);
    }

    @Bindable
    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
        notifyPropertyChanged(BR.translate);
    }

    @Bindable
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
        notifyPropertyChanged(BR.background);
    }

    @Bindable
    public String getAppreciate() {
        return appreciate;
    }

    public void setAppreciate(String appreciate) {
        this.appreciate = appreciate;
        notifyPropertyChanged(BR.appreciate);
    }

}
