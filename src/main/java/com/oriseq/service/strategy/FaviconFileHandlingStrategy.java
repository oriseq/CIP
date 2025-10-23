package com.oriseq.service.strategy;

import com.oriseq.common.utils.FileDeletionUtil;
import com.oriseq.common.utils.FileNotFoundException;
import com.oriseq.common.utils.NotAFileException;
import com.oriseq.dtm.entity.FileInfo;
import com.oriseq.service.IFileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Favicon文件处理策略类
 * 负责处理favicon文件的删除和更新操作
 *
 * @author huang
 */
@Slf4j
@Component
public class FaviconFileHandlingStrategy {

    @Autowired
    private IFileInfoService fileInfoService;

    /**
     * 删除旧的favicon文件
     *
     * @param oldFileId 旧文件ID
     */
    public void deleteOldFile(String oldFileId) {
        if (oldFileId != null && !oldFileId.isEmpty()) {
            try {
                log.debug("真实删除文件:" + oldFileId);
                FileInfo fileInfo = fileInfoService.getById(oldFileId);
                if (fileInfo == null) {
                    log.error("文件记录不存在: " + oldFileId);
                    throw new RuntimeException("文件记录不存在");
                }
                // 删除真实文件
                String path = fileInfo.getPath();
                try {
                    FileDeletionUtil.deleteFile(path);
                } catch (IOException e) {
                    log.error("删除文件失败", e);
                    throw new RuntimeException("删除文件失败", e);
                } catch (NotAFileException | FileNotFoundException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException("文件不存在");
                }
                // 删除记录
                fileInfoService.removeById(oldFileId);
                log.info("成功删除旧favicon文件: {}", oldFileId);
            } catch (Exception e) {
                log.error("删除旧favicon文件失败: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 设置新favicon文件为使用状态
     *
     * @param newFileId 新文件ID
     */
    public void setNewFileAsUsed(String newFileId) {
        if (newFileId != null && !newFileId.isEmpty()) {
            try {
                FileInfo fileInfo = fileInfoService.getById(newFileId);
                if (fileInfo != null) {
                    fileInfo.setIsUse(true);
                    fileInfoService.updateById(fileInfo);
                    log.info("成功设置新favicon文件为使用状态: {}", newFileId);
                } else {
                    log.warn("未找到ID为{}的文件记录", newFileId);
                }
            } catch (Exception e) {
                log.error("设置新favicon文件为使用状态失败: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 完整处理favicon文件更新流程
     * 先删除旧文件，再设置新文件为使用状态
     *
     * @param oldFileId 旧文件ID
     * @param newFileId 新文件ID
     */
    public void handleFaviconUpdate(String oldFileId, String newFileId) {
        // 删除旧文件
        deleteOldFile(oldFileId);

        // 设置新文件为使用状态
        setNewFileAsUsed(newFileId);
    }
}