package com.program.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author linxf
 * @date 2024/8/11
 * Jsoup 提取HTML页面信息
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
    public static List<String> getIcon(String html, String url, Integer maxSize) {
        Document doc = Jsoup.parse(html);
        ArrayList<String> iconList = new ArrayList<>();

        // 提取图标
        Elements icons = doc.select("link[type=image/x-icon],link[rel=shortcut icon],link[rel=icon]");
        for (Element icon : icons) {
            String href = icon.attr("href");
            if (href.isEmpty() ) {
                continue;
            }
            if (href.startsWith("http") || href.startsWith("//")) {
                iconList.add(href);
            } else {
                iconList.add(url + href);
            }
        }
        if (icons.isEmpty()){
            // 默认图标
            iconList.add(url+"/favicon.ico");
        }
        // 提取图片
        Elements images = doc.select("img[src$=.png], img[src$=.jpg]");
        for (Element image : images) {
            String src = image.attr("src");
            if (src.isEmpty() ) {
                continue;
            }
            if (src.startsWith("http") || src.startsWith("//")) {
                iconList.add(src);
            } else {
                iconList.add(url + src);
            }
        }
        // 去重
        List<String> uniqueList = iconList.stream().distinct().collect(Collectors.toList());
        // 截取
        if (uniqueList.size() > maxSize) {
            uniqueList = uniqueList.subList(0, maxSize);
        }

        return uniqueList;

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
