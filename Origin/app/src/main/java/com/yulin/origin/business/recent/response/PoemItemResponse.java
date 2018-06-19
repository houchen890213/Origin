package com.yulin.origin.business.recent.response;

/**
 * Created by liu_lei on 2017/5/29.
 */

public class PoemItemResponse {

/*
    {
        "poemId": 101,
            "title": "卜算子-咏梅",
            "author": "陆游",
            "dynasty": "宋代",
            "lastRead": 1491658754
    }
    */

    private long poemId;
    private String title;
    private String author;
    private String dynasty;
    private long lastRead;

    public PoemItemResponse(long poemId, String title, String author, String dynasty, long lastRead) {
        this.poemId = poemId;
        this.title = title;
        this.author = author;
        this.dynasty = dynasty;
        this.lastRead = lastRead;
    }

    public PoemItemResponse() {
    }

    public long getPoemId() {
        return poemId;
    }

    public void setPoemId(long poemId) {
        this.poemId = poemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public long getLastRead() {
        return lastRead;
    }

    public void setLastRead(long lastRead) {
        this.lastRead = lastRead;
    }

    @Override
    public String toString() {
        return "PoemItemResponse{" +
                "poemId=" + poemId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", dynasty='" + dynasty + '\'' +
                ", lastRead=" + lastRead +
                '}';
    }

}
