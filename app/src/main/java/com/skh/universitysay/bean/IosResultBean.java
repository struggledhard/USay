package com.skh.universitysay.bean;

import java.util.List;

/**
 * Created by SKH on 2017/3/27 0027.
 * IOS实体类
 */

public class IosResultBean {
    private boolean error;
    private List<IosItemBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<IosItemBean> getResults() {
        return results;
    }

    public void setResults(List<IosItemBean> results) {
        this.results = results;
    }
}
