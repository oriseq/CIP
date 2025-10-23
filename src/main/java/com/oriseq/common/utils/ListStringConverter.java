package com.oriseq.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStringConverter {

    /**
     * 将 List 转换为逗号分隔的 String。
     *
     * @param list 要转换的 List。可以是任何类型的 List，但最终会转换为 String 表示。
     * @param <T>  List 中元素的类型。
     * @return 逗号分隔的 String，如果 List 为 null 或为空，则返回空字符串。
     */
    public static <T> String listToString(List<T> list) {
        if (list == null || list.isEmpty()) {
            return ""; // 返回空字符串而不是 null，更安全
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(","); // 最后一个元素后不加逗号
            }
        }
        return sb.toString();
    }

    /**
     * 将逗号分隔的 String 转换为 List<String>。
     *
     * @param str 逗号分隔的 String。
     * @return List<String>，如果 String 为 null 或为空，则返回空 List。
     */
    public static List<String> stringToList(String str) {
        if (str == null || str.isEmpty()) {
            return new ArrayList<>(); // 返回空 List 而不是 null，更安全
        }
        String[] strArray = str.split(","); // 使用逗号分隔字符串
        return new ArrayList<>(Arrays.asList(strArray)); // 将数组转换为 List
    }

    public static void main(String[] args) {
        // 示例用法

        // List 转 String
        List<String> stringList = Arrays.asList("apple", "banana", "orange");
        String commaString = listToString(stringList);
        System.out.println("List 转 String: " + commaString); // 输出: List 转 String: apple,banana,orange

        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        String commaIntegerString = listToString(integerList);
        System.out.println("Integer List 转 String: " + commaIntegerString); // 输出: Integer List 转 String: 1,2,3,4,5

        List<Object> objectList = new ArrayList<>();
        objectList.add("hello");
        objectList.add(123);
        objectList.add(true);
        String commaObjectString = listToString(objectList);
        System.out.println("Object List 转 String: " + commaObjectString); // 输出: Object List 转 String: hello,123,true

        List<String> emptyList = new ArrayList<>();
        String emptyStringFromList = listToString(emptyList);
        System.out.println("空 List 转 String: " + emptyStringFromList); // 输出: 空 List 转 String:

        String nullListToString = listToString(null);
        System.out.println("Null List 转 String: " + nullListToString); // 输出: Null List 转 String:


        // String 转 List
        String strToList = "red,green,blue";
        List<String> convertedList = stringToList(strToList);
        System.out.println("String 转 List: " + convertedList); // 输出: String 转 List: [red, green, blue]

        String emptyString = "";
        List<String> emptyListFromString = stringToList(emptyString);
        System.out.println("空 String 转 List: " + emptyListFromString); // 输出: 空 String 转 List: []

        String nullStringToList = stringToList(null).toString();
        System.out.println("Null String 转 List: " + nullStringToList); // 输出: Null String 转 List: []


    }
}