package com.skh.universitysay.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by SKH on 2017/3/14 0014.
 * 获取具体博客内容
 */

public class ContentSpliderUtils {

    private String articleContent;

    public String getContent(String contentUrl) {

        try {
            Document document = Jsoup.connect(contentUrl).get();
            Elements details_ele = document.getElementsByAttributeValue("id", "article_details");
            Element article_ele = details_ele.get(0);
            articleContent = article_ele.toString();
            return articleContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
