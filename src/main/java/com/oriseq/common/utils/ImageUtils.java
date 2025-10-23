package com.oriseq.common.utils;

public class ImageUtils {
    public static boolean isImageBySuffix(String fileName) {
        String[] imageSuffixes = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".svg"};
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        for (String imageSuffix : imageSuffixes) {
            if (suffix.equalsIgnoreCase(imageSuffix)) {
                return true;
            }
        }
        return false;
    }
}