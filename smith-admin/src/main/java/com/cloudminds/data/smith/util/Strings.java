package com.cloudminds.data.smith.util;

import cn.hutool.core.util.StrUtil;

/**
 * String处理工具类，基于hutool的StrUtil扩展一些自己的方法
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:58
 */
public class Strings extends StrUtil {

    /**
     * 对象默认值
     *
     * @param object
     * @return
     */
    public static String toString(final Object object) {
        return object == null ? "" : object.toString();
    }

    /**
     * 对象默认值
     *
     * @param object
     * @return
     */
    public static String toString(final Object object, final String defaultStr) {
        return object == null ? defaultStr : object.toString();
    }

    /**
     * 不相等
     *
     * @param cs1
     * @param cs2
     * @return
     */
    public static boolean isNotEquals(CharSequence cs1, CharSequence cs2) {
        return !equals(cs1, cs2);
    }

}
