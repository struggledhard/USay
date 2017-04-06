package com.skh.universitysay.bean;

import java.util.List;

/**
 * Created by SKh on 2017/3/28 0028.
 * web实体类
 */

public class WebResultBean {
    private boolean error;
    private List<WebItemBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<WebItemBean> getResults() {
        return results;
    }

    public void setResults(List<WebItemBean> results) {
        this.results = results;
    }
}
