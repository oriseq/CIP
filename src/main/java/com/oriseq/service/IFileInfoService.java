package com.oriseq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.entity.FileInfo;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:36
 */
public interface IFileInfoService extends IService<FileInfo> {
    /**
     * 更新所属的样本id
     *
     * @param fileId
     * @param sampleId
     */
    void updateBelongSampleId(String fileId, Long sampleId);


    /**
     * 删除文件
     *
     * @param fileId
     */
    void deleteFile(String fileId);

    void deleteAnnexFile(Long sampleId, String fileId);

    void deleteReportFile(Long sampleId, String fileId);

    void addAnnexFile(String fileId, Long sampleId);

}
