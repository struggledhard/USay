package com.skh.universitysay.bean;

import java.util.List;

/**
 * Created by SKH on 2017/3/28 0028.
 * 拓展资源实体类
 */

public class ExpandResultBean {
    private boolean error;
    private List<ExpandItemBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ExpandItemBean> getResults() {
        return results;
    }

    public void setResults(List<ExpandItemBean> results) {
        this.results = results;
    }
}
