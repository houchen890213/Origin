package com.yulin.origin.business.content.response;

import com.yulin.origin.network.NetworkManager;
import com.yulin.origin.network.service.PoemService;

import io.reactivex.Observable;

/**
 * Created by liu_lei on 2017/6/29.
 */

public class PoemContentResponse {

    private long poemId;
    private String title;
    private String subTitle;
    private String dynasty;
    private String author;
    private String genre;
    private String motif;
    private String intro;
    private String body;
    private String annotation;
    private String translate;
    private String appreciation;
    private String background;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "PoemContentResponse{" +
                "poemId=" + poemId +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", dynasty='" + dynasty + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", motif='" + motif + '\'' +
                ", intro='" + intro + '\'' +
                ", body='" + body + '\'' +
                ", background='" + background + '\'' +
                ", annotation='" + annotation + '\'' +
                ", translate='" + translate + '\'' +
                ", appreciation='" + appreciation + '\'' +
                '}';
    }

    public static Observable<PoemContentResponse> providePoemContent() {
        return NetworkManager.getInstance().getService(PoemService.class).getPoemContent();
    }

}
