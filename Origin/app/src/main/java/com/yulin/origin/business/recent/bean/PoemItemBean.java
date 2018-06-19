package com.yulin.origin.business.recent.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.yulin.common.utils.DateUtil;
import com.yulin.origin.BR;

/**
 * Created by liu_lei on 2017/6/28.
 */

public class PoemItemBean extends BaseObservable {

    private long poemId;
    private String title;
    private String author;
    private String dynasty;
    private long lastRead;
    private String lastReadTime;

    public long getPoemId() {
        return poemId;
    }

    public void setPoemId(long poemId) {
        this.poemId = poemId;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
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

    public long getLastRead() {
        return lastRead;
    }

    public void setLastRead(long lastRead) {
        this.lastRead = lastRead;
        setLastReadTime(DateUtil.formatTime(lastRead));
    }

    @Bindable
    public String getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
        notifyPropertyChanged(BR.lastReadTime);
    }

}
