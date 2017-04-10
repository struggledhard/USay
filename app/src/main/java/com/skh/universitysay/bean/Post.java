package com.skh.universitysay.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by SKH on 2017/4/10 0010.
 * 帖子
 */

public class Post extends BmobObject{
    private String content;// 帖子内容
    private MyUser author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户
    private BmobFile image;//帖子图片
    private Integer commentNum;// 评论数
    private Integer likeNum;// 点赞数
    private BmobRelation likes;//多对多关系：用于存储喜欢该帖子的所有用户

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}