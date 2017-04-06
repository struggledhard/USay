package com.skh.universitysay.bean;

import java.util.List;

/**
 * Created by SKH on 2017/3/22 0022.
 *
 * "error": false,
 *"results": [
 *{
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
 *}
 */

public class AndroidResultBean {
    private boolean error;
    private List<AndroidItemBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<AndroidItemBean> getResults() {
        return results;
    }

    public void setResults(List<AndroidItemBean> results) {
        this.results = results;
    }
}
