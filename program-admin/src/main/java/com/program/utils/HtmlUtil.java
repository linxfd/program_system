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
     * @param doc
     * @return
     */
    public static String getTitle(Document doc) {
        Element title = doc.select("title").first();
        if (EmptyUtil.isEmpty(title)) {
            return "";
        }
        String pageTitle = title.text();
        return pageTitle;
    }


    /**
     * 获得HTML中的图标
     * @param doc
     * @return
     */
    public static List<String> getIcon(Document doc, Integer maxSize) {
        ArrayList<String> iconList = new ArrayList<>();
        // 提取图标, 包括link标签和img标签
        Elements icons = doc.select("link[type=image/x-icon],link[rel~=icon],img");
        // 默认添加一个图标
        iconList.add(doc.location()+"/favicon.ico");
        for (Element icon : icons) {
            // abs:是获得绝对路径
            String href = icon.attr("abs:href");
            String src = icon.attr("abs:src");
            if (EmptyUtil.isNotEmpty(href)) {
                iconList.add(href);
            }else if (EmptyUtil.isNotEmpty(src)) {
                iconList.add(src);
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
     * @param doc
     * @return
     */
    public static String getMeta(Document doc) {
        Elements metas = doc.select("meta[name=description]");
        if (EmptyUtil.isEmpty(metas)) {
            return "";
        }
        return metas.attr("content");
    }
}
