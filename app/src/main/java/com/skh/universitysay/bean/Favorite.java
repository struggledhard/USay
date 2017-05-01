package com.skh.universitysay.bean;

import java.io.Serializable;

/**
 * Created by SKH on 2017/4/30 0030.
 * 收藏实体类
 */

public class Favorite implements Serializable{
    private String newId;
    private String author;
    private String url;
    private String image;
    private String title;
    private boolean isClick;

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
