package com.program.utils;

/**
 * @version 1.0
 * 判断对象的工具类
 */
public class NotUtils{

    /**
     * 判断是否为空
     * 空为true，不为空返回false
     * @param str 判断对象
     */
    public static <T> boolean  isNotUtils(T str) {
        if (str == null || str.equals("") || str.equals("null")) {
            return true;
        } else {
            return false;
        }
    }
}
