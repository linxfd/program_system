package com.program.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author linxf
 * @date 2024/8/11
 * 对HTML进行解析
 */
public class HtmlUtil {

    /**
     * 获得HTML中的标题
     * @param html
     * @return
     */
    public static String getTitle(String html) {
        Document doc = Jsoup.parse(html);
        Element title = doc.select("title").first();
        String pageTitle = title.text();
        return pageTitle;
    }


    /**
     * 获得HTML中的图标
     * @param html
     * @param url
     * @return
     */
    public static List<String> getIcon(String html, String url) {
        Document doc = Jsoup.parse(html);
        HashSet hashSet = new HashSet<String>();
        // 默认图标
        hashSet.add(url+"/favicon.ico");
        // 提取图标
        Elements icons = doc.select("link[type=image/x-icon],link[rel=shortcut icon]");
        for (Element icon : icons) {
            String href = icon.attr("href");
            if (!href.isEmpty() && !href.startsWith("/favicon.ico")) {
                hashSet.add(href);
            }
        }
        // 提取图片
        Elements images = doc.select("img[src$=.png], img[src$=.jpg]");
        for (Element image : images) {
            String src = image.attr("src");
            if (!src.isEmpty()) {
                hashSet.add(src);
            }
        }

        return new ArrayList<>(hashSet);
    }

    /**
     * 获得HTML中的 描述description
     * @param html
     * @return
     */
    public static String getMeta(String html) {
        Document doc = Jsoup.parse(html);
        Elements metas = doc.select("meta[name=description]");
        return metas.attr("content");
    }
}
