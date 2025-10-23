package com.oriseq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.common.utils.FileDeletionUtil;
import com.oriseq.common.utils.FileNotFoundException;
import com.oriseq.common.utils.ListStringConverter;
import com.oriseq.common.utils.NotAFileException;
import com.oriseq.dtm.entity.FileInfo;
import com.oriseq.dtm.entity.Sample;
import com.oriseq.dtm.entity.SampleProject;
import com.oriseq.dtm.enums.ProjectStatus;
import com.oriseq.mapper.FileInfoMapper;
import com.oriseq.service.IFileInfoService;
import com.oriseq.service.ISampleProjectService;
import com.oriseq.service.ISampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/6/27 11:09
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {


    @Lazy
    @Autowired
    private ISampleService sampleService;

    @Lazy
    @Autowired
    private ISampleProjectService sampleProjectService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void updateBelongSampleId(String fileId, Long sampleId) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setSampleId(sampleId);
        fileInfo.setId(fileId);
        fileInfo.setIsUse(true);
        int i = this.baseMapper.updateById(fileInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFile(String fileId) {
        log.debug("真实删除文件:" + fileId);
        FileInfo fileInfo = baseMapper.selectById(fileId);
        if (fileInfo == null) {
            log.error("文件记录不存在: " + fileId);
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
        baseMapper.deleteById(fileId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAnnexFile(Long sampleId, String fileId) {
        // 处理sample的附件id
        Sample byId = sampleService.getById(sampleId);
        String annexFileId = byId.getAnnexFileId();
        // 将字符串转换为可变的列表
        List<String> fileIdList = ListStringConverter.stringToList(annexFileId);
        // 避免删除不存在的文件
        if (!fileIdList.contains(fileId)) {
            throw new RuntimeException("删除的文件不存在");
        }
        fileIdList.remove(fileId.toString());
        String join = String.join(",", fileIdList);
        Sample sample = new Sample();
        sample.setId(sampleId);
        sample.setAnnexFileId(join);
        sampleService.updateById(sample);
        // 删除记录和文件
        this.deleteFile(fileId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteReportFile(Long sampleId, String fileId) {
        // 1.更新样本报告记录
        Sample byId = sampleService.getById(sampleId);
        String reportIds = byId.getReportId();
        // 将字符串转换为可变的列表
        List<String> fileIdList = ListStringConverter.stringToList(reportIds);
        // 避免删除不存在的文件
        if (!fileIdList.contains(fileId)) {
            throw new RuntimeException("删除的文件不存在");
        }
        fileIdList.remove(fileId.toString());
        String newReportIds = ListStringConverter.listToString(fileIdList);
        Sample sample = new Sample();
        sample.setId(sampleId);
        sample.setReportId(newReportIds);
        sampleService.updateById(sample);
        // 2.更新样本项目报告记录
        List<SampleProject> list = sampleProjectService.list(new LambdaQueryWrapper<SampleProject>().eq(SampleProject::getSampleId, sampleId)
                .eq(SampleProject::getFileId, fileId));
        List<Long> sampleProjectIds = list.stream().map(SampleProject::getId).toList();
        LambdaUpdateWrapper<SampleProject> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SampleProject::getId, sampleProjectIds);
        sampleProjectService.update(updateWrapper.set(SampleProject::getFileId, null)
                .set(SampleProject::getProjectStatus, ProjectStatus.CONFIRMING.getValue()));
        sampleService.updateSampleStatus(sampleId);
        // 使用代理对象调用 deleteFile 方法, 保证事务
        FileInfoServiceImpl proxy = applicationContext.getBean(FileInfoServiceImpl.class);
        proxy.deleteFile(fileId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAnnexFile(String fileId, Long sampleId) {
        Sample sample = sampleService.getById(sampleId);
        // 新增附件id
        String annexFileId = sample.getAnnexFileId();
        List<String> strings = ListStringConverter.stringToList(annexFileId);
        strings.add(fileId);
        String s = ListStringConverter.listToString(strings);
        sample.setAnnexFileId(s);
        // 3. 保存更新后的样本信息
        sampleService.updateById(sample);
        // 4. 更新文件信息
        FileInfo fileInfo = baseMapper.selectById(fileId);
        fileInfo.setSampleId(sampleId);
        baseMapper.updateById(fileInfo);
    }
}
