package com.skh.universitysay.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SKH on 2017/3/22 0022.
 *"_id": "58cbf72b421aa90f13178688",
 *"createdAt": "2017-03-17T22:48:11.282Z",
 *"desc": "Android\u5b58\u50a8\u8def\u5f84\u4f60\u4e86\u89e3\u591a\u5c11\uff1f",
 *"images": [
 *"http://img.gank.io/f58881ff-000f-4e7e-8b72-25112be5b830"
 *],
 *"publishedAt": "2017-03-21T12:19:46.895Z",
 *"source": "web",
 *"type": "Android",
 *"url": "http://itfeifei.win/2017/03/17/Android%E5%AD%98%E5%82%A8%E8%B7%AF%E5%BE%84%E4%BD%A0%E4%BA%86%E8%A7%A3%E5%A4%9A%E5%B0%91/",
 *"used": true,
 *"who": null
 */

public class AndroidItemBean implements Serializable{
    private String _id;
    private String createdAt;
    private String desc;
    private List<String> images;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
