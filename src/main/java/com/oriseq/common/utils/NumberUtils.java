package com.oriseq.common.utils;

/**
 * 数字工具类
 *
 * @author hacah
 * @version 1.0
 * @date 2025/4/2 11:03
 */
public class NumberUtils {

    public static String zfill(int number, int digits, char fillChar) {
        String str = String.valueOf(number);
        if (str.length() < digits) {
            StringBuilder builder = new StringBuilder();
            while (builder.length() + str.length() < digits) {
                builder.append(fillChar);
            }
            builder.append(str);
            return builder.toString();
        } else {
            return str;
        }
    }
}
