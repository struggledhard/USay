package com.skh.universitysay.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by SKH on 2017/4/26 0026.
 * 反馈数据
 */

public class FeedBack extends BmobObject{
    private MyUser mUser;    // 反馈者
    private String feedBack;  // 反馈内容

    public MyUser getUser() {
        return mUser;
    }

    public void setUser(MyUser user) {
        mUser = user;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }
}
