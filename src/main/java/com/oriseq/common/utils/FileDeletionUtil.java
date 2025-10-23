package com.oriseq.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author huang
 */
public class FileDeletionUtil {

    /**
     * 删除指定路径的文件
     *
     * @param filePath 文件路径
     * @return 如果文件成功删除则返回true，否则返回false
     * @throws IllegalArgumentException 如果文件路径为空或无效
     * @throws IOException              如果文件删除过程中发生IO错误
     * @throws NotAFileException        如果指定路径是一个目录，而不是文件
     * @throws FileNotFoundException    如果文件不存在
     */
    public static boolean deleteFile(String filePath) throws IllegalArgumentException, IOException, NotAFileException, FileNotFoundException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        Path path = Paths.get(filePath);
        File file = path.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在: " + filePath);
        }

        if (file.isDirectory()) {
            throw new NotAFileException("指定路径是一个目录，而不是文件: " + filePath);
        }

        try {
            if (Files.deleteIfExists(path)) {
                System.out.println("文件已成功删除: " + filePath);
                return true;
            } else {
                throw new IOException("文件删除失败: " + filePath);
            }
        } catch (IOException e) {
            throw new IOException("删除文件时发生错误: " + filePath, e);
        }
    }

    public static void main(String[] args) {
        // 示例调用
        try {
            String filePath = "C:\\Users\\huang\\Downloads\\送检任务管理表_分类仅自检_20250324103035.xlsx";
            boolean isDeleted = deleteFile(filePath);
            System.out.println("删除结果: " + isDeleted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

