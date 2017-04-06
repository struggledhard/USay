package com.skh.universitysay.bean;

/**
 * Created by SKH on 2017/3/12 0012.
 * 博客头条实体类
 */

public class NewsItems {
    private int id;
    /**
     * 作者ID
     */
    private String authorId;
    /**
     * 标题
     */
    private String title;
    /**
     * 具体内容链接
     */
    private String contentUrl;
    /**
     * 摘要
     */
    private String description;
    /**
     * 发布日期
     */
    private String date;
    /**
     * 头像图片的链接
     */
    private String headImgLink;
    /**
     * 浏览人数
     */
    private String viewPerson;
    /**
     * 文章分类类型
     *
     */
    private String newsType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeadImgLink() {
        return headImgLink;
    }

    public void setHeadImgLink(String headImgLink) {
        this.headImgLink = headImgLink;
    }

    public String getViewPerson() {
        return viewPerson;
    }

    public void setViewPerson(String viewPerson) {
        this.viewPerson = viewPerson;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    @Override
    public String toString() {
        String result;
        result = "NewsItem [id=" + id + ", authorId=" + authorId
                + ", title=" + title + ", contentUrl=" + contentUrl
                + ", date=" + date + ", headImgLink=" + headImgLink
                + ", description=" + description + ", newsType=" + newsType
                + ", viewPerson=" + viewPerson +"]";
        return result;
    }
}
