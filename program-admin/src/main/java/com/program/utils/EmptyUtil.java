package com.program.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @version 1.0
 * 判断对象的工具类
 */
public class EmptyUtil {

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

    /**
     * 对象空判断，为空时返回true
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if(object instanceof Collection) {	//List和Set
            if(((Collection)object).isEmpty()) {
                return true;
            }
        } else if(object instanceof Map) {	//Map
            if(((Map)object).isEmpty()) {
                return true;
            }
        } else if(object.getClass().isArray()) {	//数组
            if(((Object[]) object).length == 0) {
                return true;
            }
        } else {	//普通对象
            if (object.toString().trim().equals("")) {
                return true;
            }
            if (object.toString().trim().equals("null")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象非空判断，非空时返回true
     * @param object
     * @return
     */
    public static boolean isNotEmpty(Object object) {
        if(object == null) {
            return false;
        }
        if(object instanceof Collection) {	//List和Set
            if(!((Collection)object).isEmpty()) {
                return true;
            }
        } else if(object instanceof Map) {	//Map
            if(!((Map)object).isEmpty()) {
                return true;
            }
        } else if(object.getClass().isArray()) {	//数组
            if(((Object[]) object).length > 0) {
                return true;
            }
        } else {	//普通对象
            if (!object.toString().trim().equals("") && !object.toString().trim().equals("null")) {
                return true;
            }
        }
        return false;
    }
}
