package com.skh.universitysay.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by SKH on 2017/3/31 0031.
 * 注册信息
 */

public class MyUser extends BmobUser implements Serializable{
    private String userNickName;
    private String description;
    private String headImgUrl;

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
