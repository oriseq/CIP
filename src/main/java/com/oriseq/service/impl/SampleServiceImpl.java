package com.oriseq.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.common.business.statisticalProject.StatisticalProjectRedisTool;
import com.oriseq.common.logisticsInformation.QueryLogisticsInformation1;
import com.oriseq.common.utils.ListStringConverter;
import com.oriseq.common.utils.NumberUtils;
import com.oriseq.dtm.dto.GroupUsersCascaderDTO;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.SampleDTO;
import com.oriseq.dtm.entity.Package;
import com.oriseq.dtm.entity.*;
import com.oriseq.dtm.enums.InspectionScene;
import com.oriseq.dtm.enums.ProjectStatus;
import com.oriseq.dtm.enums.SampleStatus;
import com.oriseq.dtm.vo.*;
import com.oriseq.dtm.vo.sample.*;
import com.oriseq.mapper.PackageMapper;
import com.oriseq.mapper.SampleMapper;
import com.oriseq.mapper.SampleProjectMapper;
import com.oriseq.mapper.UsersMapper;
import com.oriseq.service.*;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 16:59
 */
@Service
public class SampleServiceImpl extends ServiceImpl<SampleMapper, Sample> implements ISampleService {

    @Autowired
    private ISampleProjectService sampleProjectService;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private ISampleHostingService sampleHostingService;
    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private PackageMapper packageMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private SampleProjectMapper sampleProjectMapper;
    @Autowired
    private StatisticalProjectRedisTool statisticalProjectRedisTool;
    @Autowired
    private MessageNotificationService messageNotificationService;
    @Autowired
    private IUserGroupService userGroupService;
    @Autowired
    private IProjectService projectService;

    /**
     * 构建查询条件
     *
     * @param sampleRequestVO
     * @param objectQueryWrapper
     */
    private static void buildQW(SampleRequestVO sampleRequestVO, QueryWrapper<Object> objectQueryWrapper) {
        // 其他查询条件
        Optional.ofNullable(sampleRequestVO).ifPresent(sample -> {
            objectQueryWrapper.eq(true, "1", "1");
            objectQueryWrapper.like(StrUtil.isNotBlank(sample.getName()), "name", sample.getName());
            objectQueryWrapper.eq(StrUtil.isNotBlank(sample.getSex()), "sex", sample.getSex());
            objectQueryWrapper.eq(Objects.nonNull(sample.getAge()), "age", sample.getAge());
            objectQueryWrapper.eq(Objects.nonNull(sample.getBirthday()), "birthday", sample.getBirthday());
            objectQueryWrapper.eq(StrUtil.isNotBlank(sample.getStatus()), "status", sample.getStatus());
            objectQueryWrapper.like(StrUtil.isNotBlank(sample.getLogisticsTrackingNumber()), "logistics_tracking_number", sample.getLogisticsTrackingNumber());
            objectQueryWrapper.like(StrUtil.isNotBlank(sample.getRemarks()), "s.remarks", sample.getRemarks());
            objectQueryWrapper.eq(StrUtil.isNotBlank(sample.getSampleId()), "s.sample_id", sample.getSampleId());
            objectQueryWrapper.eq(Objects.nonNull(sample.getSampleUserGroupId()), "s.sample_user_group_id", sample.getSampleUserGroupId());
            if (sample.getSubmissionTime() != null && sample.getSubmissionTime().size() == 2) {
                objectQueryWrapper.ge("submission_time", sample.getSubmissionTime().get(0));
                objectQueryWrapper.le("submission_time", sample.getSubmissionTime().get(1));
            }
            if (sample.getUpdateTime() != null && sample.getUpdateTime().size() == 2) {
                objectQueryWrapper.ge("s.update_time", sample.getUpdateTime().get(0));
                objectQueryWrapper.le("s.update_time", sample.getUpdateTime().get(1));
            }

            objectQueryWrapper.orderBy(
                    StrUtil.isNotBlank(sampleRequestVO.getField()) && StrUtil.isNotBlank(sampleRequestVO.getOrder()),
                    Objects.equals("ascend", sampleRequestVO.getOrder()) ? true : false,
                    sampleRequestVO.getField());
            // 场景分隔：非全部完成、全部完成
            Optional.ofNullable(sample.getInspectionScene()).ifPresent(inspectionScene -> {
                if (Objects.equals(InspectionScene.INSPECTION.getCode(), inspectionScene)) {
                    objectQueryWrapper.ne("s.status", SampleStatus.FULLY_COMPLETED.getValue());
                } else if (Objects.equals(InspectionScene.ARCHIVE.getCode(), inspectionScene)) {
                    objectQueryWrapper.eq("s.status", SampleStatus.FULLY_COMPLETED.getValue());
                }
            });

            // 和项目相关的查询条件(后添加)
            // 1.仪器：包含所选仪器对应项目的样本
            if (sample.getInstrumentIds() != null && !sample.getInstrumentIds().isEmpty()) {
                String instrumentInClause = sample.getInstrumentIds().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                objectQueryWrapper.and(wrapper -> wrapper
                        .exists("SELECT 1 FROM sample_project sp "
                                + "JOIN project_master pm ON sp.project_id = pm.id "
                                + "WHERE sp.sample_id = s.id AND sp.is_del = 0 "
                                + "AND pm.instrument_id IN (" + instrumentInClause + ")")
                );
            }
            // 2.外送单位：包含所选外送单位的项目的样本
            // 外送单位条件：deliveryUnitIds
//            if (sample.getDeliveryUnitIds() != null && !sample.getDeliveryUnitIds().isEmpty()) {
//                String deliveryUnitsInClause = sample.getDeliveryUnitIds().stream()
//                        .map(id -> {
//                            return "JSON_CONTAINS(delivery_unit_ids, '" + id + "')";
//                        })
//                        .collect(Collectors.joining(" or "));
//
//                objectQueryWrapper.and(wrapper -> wrapper
//                        .exists("SELECT 1 FROM sample_project sp "
//                                + "JOIN project_master pm ON sp.project_id = pm.id "
//                                + "WHERE sp.sample_id = s.id AND sp.is_del = 0 "
//                                + "AND " + deliveryUnitsInClause)
//                );
//            }
            // 外送单位条件：deliveryUnits
            if (sample.getDeliveryUnits() != null && !sample.getDeliveryUnits().isEmpty()) {
                String deliveryUnitsInClause = sample.getDeliveryUnits().stream()
                        .map(id -> "'" + id + "'")
                        .collect(Collectors.joining(","));
                objectQueryWrapper.and(wrapper -> wrapper
                        .exists("SELECT 1 FROM sample_project sp "
                                + "WHERE sp.sample_id = s.id AND sp.is_del = 0 "
                                + "AND sp.delivery_unit in (" + deliveryUnitsInClause + ")")
                );
            }

            // 3. 项目状态条件
            if (sample.getProjectStatuses() != null && !sample.getProjectStatuses().isEmpty()) {
                String statusInClause = sample.getProjectStatuses().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                objectQueryWrapper.and(wrapper -> wrapper
                        .exists("SELECT 1 FROM sample_project sp "
                                + "WHERE sp.sample_id = s.id AND sp.is_del = 0 "
                                + "AND sp.project_status IN (" + statusInClause + ")")
                );
            }

        });
        // 必须保留，否则会报错，除非更改xml
        objectQueryWrapper.groupBy(true, "s.id");
        // 如果样本所有項目為取消时，不返回项目。返回空项目数据
        objectQueryWrapper.having(
                "non_zero_project_count > 0\n" +
                        "OR COUNT(sp.id) = 0"
        );
        // 查询非删除数据
        objectQueryWrapper.eq(true, "s.is_del", 0);

    }

    /**
     * 项目状态统计
     *
     * @param projects 项目集合
     * @return
     */
    private static List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatistics(List<SampleProjectVO> projects) {
        if (!projects.isEmpty()) {
            // 构建返回数据
            HashMap<Integer, Integer> numberMap = new HashMap<>();
            numberMap.put(ProjectStatus.CANCELLED.getValue(), 0);
            numberMap.put(ProjectStatus.PENDING.getValue(), 0);
            numberMap.put(ProjectStatus.CONFIRMING.getValue(), 0);
            numberMap.put(ProjectStatus.IN_PROGRESS.getValue(), 0);
            numberMap.put(ProjectStatus.COMPLETED.getValue(), 0);
            for (SampleProjectVO project : projects) {
                if (project.getProjectStatus().equals(ProjectStatus.CANCELLED.getValue())) {
                    numberMap.put(ProjectStatus.CANCELLED.getValue(), numberMap.get(ProjectStatus.CANCELLED.getValue()) + 1);
                } else if (project.getProjectStatus().equals(ProjectStatus.PENDING.getValue())) {
                    numberMap.put(ProjectStatus.PENDING.getValue(), numberMap.get(ProjectStatus.PENDING.getValue()) + 1);
                } else if (project.getProjectStatus().equals(ProjectStatus.CONFIRMING.getValue())) {
                    numberMap.put(ProjectStatus.CONFIRMING.getValue(), numberMap.get(ProjectStatus.CONFIRMING.getValue()) + 1);
                } else if (project.getProjectStatus().equals(ProjectStatus.IN_PROGRESS.getValue())) {
                    numberMap.put(ProjectStatus.IN_PROGRESS.getValue(), numberMap.get(ProjectStatus.IN_PROGRESS.getValue()) + 1);
                } else if (project.getProjectStatus().equals(ProjectStatus.COMPLETED.getValue())) {
                    numberMap.put(ProjectStatus.COMPLETED.getValue(), numberMap.get(ProjectStatus.COMPLETED.getValue()) + 1);
                }
            }
            List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatisticsList = numberMap.entrySet().stream().map(entry -> {
                return new SampleBasicInfo.ProjectStatusStatistics(entry.getKey(), entry.getValue().intValue());
            }).toList();
            return projectStatusStatisticsList;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Sample submitInspectionForm(InspectionInfoVO inspectionInfoVO, LoginUser user) {
        /*
         * - 计算样本状态
         * - 设置《上次提交的项目组合》套餐
         * - 保存
         * */
        // 1.保存
        Sample sample = new Sample();

        BeanUtils.copyProperties(inspectionInfoVO, sample);
        sample.setUserId(Long.valueOf(user.getUserId()));
        sample.setUserGroupId(user.getUserGroupId());
        sample.setSubmissionTime(LocalDateTime.now());
        sample.setUpdateTime(LocalDateTime.now());


        // 2.计算样本状态
        /*
       样本状态
        无项目
            无记录
            有记录：非"取消"状态的项目数量 = 0
        无确认项目
            非"取消"状态的项目数量 > 0 且所有为"待定"  （待定项目>0，且取消项目>=0,其他状态不允许存在）
        等待检测
            “确认检测” > 0 & "正在检测"状态的项目数量 = 0 & “已完成”状态的项目数量 = 0
        正在检测
            "正在检测"状态的项目数量 > 0 & “已完成”状态的项目数量 = 0
        部分完成
            “确认检测”或"正在检测"状态的项目数量 > 0 & “已完成”状态的项目数量 > 0
        全部完成
            所有项目状态为"已完成"或“取消”
        样本状态
         无项目:0
        无确认项目:1
        等待检测:2
        正在检测:3
        部分完成:4
        全部完成:5

        项目状态
        - 取消:0
        - 待定:1
        - 确认检测:2
        - 正在检测:3
          - 报告路径为空
        - 已完成:4
          - 报告路径非空
         */
        List<InspectionInfoVO.Projects> selectedProjects = inspectionInfoVO.getSelectedProjects();
        // 样本状态
        int size = OptionalInt.of(selectedProjects.size()).orElse(0);
        Map<Integer, Long> statusMap = selectedProjects.stream().collect(Collectors.groupingBy(projects -> projects.getStatus(), Collectors.counting()));
        Integer status = this.getSampleStatus(size, statusMap);
        sample.setStatus(status);
        // 保证所属用户组不为空才能生成业务编号
        if (sample.getSampleUserGroupId() != null) {
            // 处理生成     样本编号（业务）、当天提交编号
            this.dealGenerateSampleNumber(sample);
            // 在服务层面去判断sample_id & is_del = 0是否唯一，避免重复提交
            String sampleId = sample.getSampleId();
            Long l = baseMapper.selectCount(new LambdaQueryWrapper<Sample>().eq(Sample::getSampleId, sampleId)
                    .eq(Sample::getIsDel, 0));
            if (l > 0) {
                throw new RuntimeException(sampleId + "，已存在");
            }
        }
        try {
            baseMapper.insert(sample);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // Extract the duplicate key value from the cause
            String duplicateKey = extractDuplicateKeyFromException(e.getCause());
            throw new RuntimeException(duplicateKey + "，已存在");
        }


        // 保存附件
        List<FileInfo> fieldsc = inspectionInfoVO.getFieldsc();
        if (fieldsc != null && !fieldsc.isEmpty()) {
//            String string = fieldsc.stream().collect(Collectors.joining(","));
//            sample.setAnnexPath(string);
            fieldsc.forEach(fileInfo -> {
                fileInfo.setSampleId(sample.getId());
                fileInfo.setIsUse(Boolean.TRUE);
                // 设置附件类型
                fileInfo.setAttachmentType();
            });
            fileInfoService.updateBatchById(fieldsc);
            // 更新样本附件路径
            String fileIds = fieldsc.stream().map(FileInfo::getId).map(Object::toString).collect(Collectors.joining(","));
            Sample sample1 = new Sample();
            sample1.setId(sample.getId());
            sample1.setAnnexFileId(fileIds);
            baseMapper.updateById(sample1);
        }


        /**
         * 3.保存项目
         */
        if (size > 0) {
            // 保存list
            List<SampleProject> sampleProjects = selectedProjects.stream().map(selectedProject -> {
                SampleProject sampleProject = new SampleProject();
                sampleProject.setProjectId(selectedProject.getId());
                sampleProject.setProjectStatus(selectedProject.getStatus());
                sampleProject.setDeliveryUnit(selectedProject.getDeliveryUnit());
                sampleProject.setDeliveryUnit(selectedProject.getDeliveryUnit());
                sampleProject.setSampleId(sample.getId());
                LocalDateTime deadline = sampleProjectService.calculateTheDeadline(selectedProject.getId(), LocalDateTime.now());
                sampleProject.setDeadline(deadline);
                return sampleProject;
            }).collect(Collectors.toList());
            sampleProjectService.saveBatch(sampleProjects);
            // 统计
            statisticalProjectRedisTool.statisticalProject(user.getUserGroupId(), sampleProjects);
            /*
               4.设置《上次提交的项目组合》套餐
             */
            String peojectIdstring = sampleProjects.stream().map(sampleProject -> {
                return sampleProject.getProjectId().toString();
            }).collect(Collectors.joining(","));
            // 是否存在上次提交的项目组合
            Package one = packageMapper.selectOne(new LambdaQueryWrapper<Package>()
                    .eq(Package::getPackageInfo, "上次提交的项目组合")
                    .and(true, wrapper -> wrapper.eq(Package::getUserId, user.getUserId())));
            // 判断是更新还是修改
            if (one != null && one.getId() != null) {
                one.setProjectIdList(peojectIdstring);
                packageMapper.updateById(one);
            } else {
                // 新增套餐
                Package aPackage = new Package();
                aPackage.setPackageInfo("上次提交的项目组合");
                aPackage.setProjectIdList(peojectIdstring);
                aPackage.setUserId(Long.valueOf(user.getUserId()));
                packageMapper.insert(aPackage);
            }


        }

        return sample;

    }

    /**
     * 处理生成     样本编号（业务）、当天提交编号   两个编号
     *
     * @param sample
     */
    private void dealGenerateSampleNumber(Sample sample) {
        boolean sampleIdIsBlank = StrUtil.isBlank(sample.getSampleId());
        boolean submitNumberTodayIsBlank = StrUtil.isBlank(sample.getSubmitNumberToday());
        boolean bTow = sampleIdIsBlank || submitNumberTodayIsBlank;
        if (bTow) {
            // 查样本表，当天不同送检单位，已提交数目，获取所有当天的数据
            List<Sample> samplesTodayByGroup = baseMapper.selectList(new LambdaQueryWrapper<Sample>()
                    .between(Sample::getSubmissionTime, DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()))
                    .eq(Sample::getSampleUserGroupId, sample.getSampleUserGroupId())
                    .orderByDesc(Sample::getSubmissionTime));

             /*
            1. 样本编号规则：提交时间+ ‘-’  + 送检单位 + 当天已提交数目加一
            例如：20250210-E11，20250210-A15，20250210-B127
            2. 当天提交编号规则：送检单位 + 当天已提交数目加一
            例如：E11，A15，B127
            // 以两位数为送检单位
            */
            /*
             1.如果当天提交编号存在，也就是用户提交了，那就引用到样本编号规则中
            * */
            UserGroup byId = userGroupService.getById(sample.getSampleUserGroupId());
            String submitNumberTodayR = sample.getSubmitNumberToday();
            if (submitNumberTodayIsBlank) {
                int nextNum = samplesTodayByGroup.size() + 1;
                submitNumberTodayR = byId.getGroupName() + NumberUtils.zfill(nextNum, 2, '0');
                sample.setSubmitNumberToday(submitNumberTodayR);
            }
            if (sampleIdIsBlank) {
                sample.setSampleId(DateUtil.format(DateUtil.date(), "yyyyMMdd") + "-" + submitNumberTodayR);
            }

        }

    }

    private String extractDuplicateKeyFromException(Throwable cause) {
        // You can modify this logic based on your specific exception hierarchy
        if (cause instanceof SQLIntegrityConstraintViolationException) {
            String message = cause.getMessage();
            int startIndex = message.indexOf("'");
            int endIndex = message.indexOf("'", startIndex + 1);
            log.error(message);
            return message.substring(startIndex, endIndex + 1);
        }
        return null;
    }

    /**
     * 类型：
     * 无项目
     * 无记录
     * 有记录：非"取消"状态的项目数量 = 0
     * 无确认项目
     * 非"取消"状态的项目数量 > 0 且所有为"待定"
     * 正在检测
     * “确认检测”和"正在检测"状态的项目数量 > 0 & “已完成”状态的项目数量 = 0
     * 部分完成
     * “确认检测”和"正在检测"状态的项目数量 > 0 & “已完成”状态的项目数量 > 0
     * 全部完成
     * 所有项目状态为"已完成"或“取消”
     *
     * @param size
     * @param statusMap
     * @return
     */
    private Integer getSampleStatus(int size, Map<Integer, Long> statusMap) {
        Integer status = null;
        if (size == 0) {
            status = SampleStatus.NO_PROJECT.getValue();
        } else {
            // 1/所有都是取消状态的项目为‘无项目’
            Long cancelled = statusMap.getOrDefault(ProjectStatus.CANCELLED.getValue(), 0L);
            // 待定项目
            Long pending = statusMap.getOrDefault(ProjectStatus.PENDING.getValue(), 0L);
            // 确认检测
            Long confirming = statusMap.getOrDefault(ProjectStatus.CONFIRMING.getValue(), 0L);
            // 正在检测
            Long inProgress = statusMap.getOrDefault(ProjectStatus.IN_PROGRESS.getValue(), 0L);
            // 已完成
            Long completed = statusMap.getOrDefault(ProjectStatus.COMPLETED.getValue(), 0L);
            if (cancelled == size) {
                status = SampleStatus.NO_PROJECT.getValue();
            } else if (pending + cancelled == size && pending > 0) {
                // 2.非"取消"状态的项目数量 > 0 且所有为"待定"   -->   '无确认项目'
                // 即除去"取消"状态的项目，所有项目都为"待定"
                status = SampleStatus.NO_CONFIRMED_PROJECT.getValue();
            } else if (confirming > 0 && inProgress == 0 && completed == 0) {
                // 3.“确认检测” > 0 & "正在检测"状态的项目数量 = 0 & “已完成”状态的项目数量 = 0  -->  '等待检测'
                status = SampleStatus.WAITING_FOR_TESTING.getValue();
            } else if (inProgress > 0 && completed == 0) {
                // 4."正在检测"状态的项目数量 > 0 & “已完成”状态的项目数量 = 0 -->  '正在检测
                status = SampleStatus.TESTING_IN_PROGRESS.getValue();
            } else if (confirming + inProgress > 0 && completed > 0) {
                // 5.“确认检测”或"正在检测"状态的项目数量 > 0 & “已完成”状态的项目数量 > 0  -->  '部分完成
                status = SampleStatus.PARTIALLY_COMPLETED.getValue();
            } else if (completed + cancelled == size) {
                // 6.所有项目状态为"已完成"或“取消” -->  '全部完成'
                status = SampleStatus.FULLY_COMPLETED.getValue();
            }
            return status;
        }
        return status;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IPage<SampleInfoVO> getSampleInfo(LoginUser user, SampleRequestVO sampleRequestVO) {
        // 判断权限 按照不同用户查询不同信息
        QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
        // 代管样本list
        List<SampleHosting> sampleHostings = sampleHostingService.list();
        SampleHostingForSample sampleHostingForSample = new SampleHostingForSample(sampleHostings, Long.valueOf(user.getUserId()));
        // 数据权限条件
        // 1.超级管理员：查询所有样本 2.内部组：查询所有样本
        if (!user.isSuper() && !user.isInternalGroup()) {
            if (user.isGroupSuper()) {
                objectQueryWrapper.eq("user_group_id", user.getUserGroupId());
            } else {
                // 代管样本的id
                Set<Long> SampleIdList = sampleHostingForSample.getSampleHostingIdsByUserId();
                // 用户本身的样本和代管的样本
                objectQueryWrapper.and(true, wrapper -> wrapper
                        .eq("user_id", user.getUserId())
                        .or()
                        .in(!SampleIdList.isEmpty(), "s.id", SampleIdList)
                );

            }
        }
        IPage<SampleInfoVO> sampleInfoVOIPage = this.getSampleInfoVOS2(user, sampleRequestVO, objectQueryWrapper, sampleHostingForSample);
        return sampleInfoVOIPage;
    }

    private IPage<SampleInfoVO> getSampleInfoVOS2(LoginUser user, SampleRequestVO sampleRequestVO, QueryWrapper<Object> objectQueryWrapper, SampleHostingForSample sampleHostingForSample) {
        this.buildQW(sampleRequestVO, objectQueryWrapper);
        Page<Sample> objectPage = new Page<>(sampleRequestVO.getPage(), sampleRequestVO.getPageSize());
        // 查询样本数据
        IPage<Sample> samplePageWithProjects = this.getSamplePageWithProjects(objectPage, objectQueryWrapper, sampleRequestVO.getProjects());
        List<Sample> records = samplePageWithProjects.getRecords();
        // 把样本数据封装成VO
        List<SampleInfoVO> collectSampleVOs = records.stream().map(sample -> {
            SampleInfoVO sampleInfoVO = new SampleInfoVO();
            BeanUtils.copyProperties(sample, sampleInfoVO);
            List<SampleProjectVO> sampleProjectVOS = sample.getProjects().stream().map(project -> {
                        SampleProjectVO sampleProjectVO = new SampleProjectVO();
                        sampleProjectVO.setSampleProjectId(project.getSampleProjectId());
                        sampleProjectVO.setProjectId(project.getId());
                        sampleProjectVO.setProjectName(project.getProjectName());
                        sampleProjectVO.setProjectStatus(project.getProjectStatus());
                        sampleProjectVO.setIsDeliveryOutside(project.getIsDeliveryOutside());
                        sampleProjectVO.setCreationTime(project.getCreationTime());
                        sampleProjectVO.setDeliveryUnit(project.getDeliveryUnit());
                        sampleProjectVO.setInstrumentId(project.getInstrumentId());
                        return sampleProjectVO;
                    }).sorted(Comparator.comparingInt((SampleProjectVO project) -> {
                        // 优先按项目状态排序
                        int status = project.getProjectStatus();
                        // 待定、确认检测、正在检测排前面
                        if (status == 1) {
                            return 0;
                        } else if (status == 2) {
                            return 1;
                        } else if (status == 3) {
                            return 2;
                        } else if (status == 4) {
                            // 已完成排中间
                            return 3;
                        } else {
                            // 取消排最后
                            return 4;
                        }
                    }).thenComparing((SampleProjectVO project) -> Boolean.TRUE.equals(project.getIsDeliveryOutside()))) // 按是否外送排序
                    .collect(Collectors.toList());
            sampleInfoVO.setProjects(sampleProjectVOS);
            return sampleInfoVO;
        }).collect(Collectors.toList());
        // 取
        for (SampleInfoVO sampleInfoVO : collectSampleVOs) {
            // 1.查询附件信息并设置
            String annexFileId = sampleInfoVO.getAnnexFileId();
            if (StringUtils.isNotBlank(annexFileId)) {
                List<String> FileIdList = Arrays.stream(annexFileId.split(",")).toList();
                LambdaQueryWrapper<FileInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(FileInfo::getId, FileInfo::getOriginalFileName, FileInfo::getSampleId, FileInfo::getAttachmentType);
                queryWrapper.eq(FileInfo::getSampleId, sampleInfoVO.getId());
                queryWrapper.in(FileInfo::getId, FileIdList);
                List<FileInfo> list = fileInfoService.list(queryWrapper);
                if (!list.isEmpty()) {
                     /*
                    imgUrls：[1,2,3,4],
                    otherFiles:[
                        {
                            url:1,
                            name:asvavae.pdf
                        },
                    ]
                 */
                    JSONObject entries = new JSONObject();
                    JSONArray imgUrls = new JSONArray();
                    JSONArray otherFiles = new JSONArray();
                    entries.set("imgUrls", imgUrls);
                    entries.set("otherFiles", otherFiles);
                    for (FileInfo fileInfo : list) {
                        if ("1".equals(fileInfo.getAttachmentType())) {
                            imgUrls.add(fileInfo.getId().toString());
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.set("url", fileInfo.getId().toString());
                            jsonObject.set("name", fileInfo.getOriginalFileName());
                            otherFiles.add(jsonObject);
                        }
                    }

                    sampleInfoVO.setFileInfo(entries);

                } else {
                    sampleInfoVO.setFileInfo(null);
                }

            }


            // 2.设置代管的状态，sampleHostingStatus：1（代管），2（被代管），3（普通）
            Set<Long> sampleHostingIdsByUserId = sampleHostingForSample.getSampleHostingIdsByUserId();
            Long sampleInfoVOId = sampleInfoVO.getId();
            if (sampleHostingIdsByUserId.contains(sampleInfoVOId)) {
                sampleInfoVO.setSampleHostingStatus("1");
            } else if (sampleHostingForSample.getBeSampleHostingIdsByUserId().contains(sampleInfoVOId)) {
                sampleInfoVO.setSampleHostingStatus("2");
            } else {
                sampleInfoVO.setSampleHostingStatus("3");
            }

            // 3.设置代管用户
            if (sampleHostingForSample.getSampleHostingsIds().contains(sampleInfoVOId)) {
                List<SampleHosting> list1 = sampleHostingForSample.getSampleHostings().stream()
                        .filter(sampleHosting -> sampleHosting.getSampleId().equals(sampleInfoVOId)).toList();
                if (!list1.isEmpty()) {
                    List<String> userNames = list1.stream().map(sampleHosting -> {
                        Long hostingUserId = sampleHosting.getHostingUserId();
                        User byId = usersService.getById(hostingUserId);
                        return byId.getUsername();
                    }).toList();
                    sampleInfoVO.setManagedUsers(userNames);
                }
            } else {
                sampleInfoVO.setManagedUsers(new ArrayList<>());
            }

            // 4.统计项目的状态
            List<SampleProjectVO> projects = sampleInfoVO.getProjects();
            List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatisticsList = projectStatusStatistics(projects);
            sampleInfoVO.setProjectStatusStatistics(projectStatusStatisticsList);

        }

        IPage<SampleInfoVO> sampleInfoVOIPage = new Page<>();
        sampleInfoVOIPage.setRecords(collectSampleVOs);
        sampleInfoVOIPage.setCurrent(samplePageWithProjects.getCurrent());
        sampleInfoVOIPage.setSize(samplePageWithProjects.getSize());
        sampleInfoVOIPage.setTotal(samplePageWithProjects.getTotal());
        sampleInfoVOIPage.setPages(samplePageWithProjects.getPages());
        return sampleInfoVOIPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IPage<Sample> getSamplePageWithProjects(IPage<Sample> page, Wrapper<Object> wrapper, List<Long> projectIds) {
        // 1. 分页查询 sample 数据
        IPage<Sample> samplePage = baseMapper.selectPageSamples(page, wrapper, projectIds);
        List<Sample> samples = samplePage.getRecords();

        if (!samples.isEmpty()) {
            // 2. 获取 sampleId 列表
            List<Long> sampleIds = samples.stream().map(Sample::getId).collect(Collectors.toList());

            // 3. 查询关联的 project 数据
            List<Project> projects = baseMapper.selectProjectsBySampleIds(sampleIds);

            // 4. 将 project 数据填充到 sample 对象中
            Map<Long, List<Project>> projectMap = projects.stream()
                    .collect(Collectors.groupingBy(Project::getSampleId));

            samples.forEach(sample -> {
                sample.setProjects(projectMap.getOrDefault(sample.getId(), Collections.emptyList()));
            });
        }

        return samplePage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSampleReportId(SampleReportIdUpdateVO sampleReportIdUpdateVO) {
        // 报告id
        String repId = sampleReportIdUpdateVO.getReportIds().get(0);
        // 查询样本报告文件Id，并判断添加
        Sample byId = this.getById(sampleReportIdUpdateVO.getSampleId());
        String reportId = byId.getReportId();
        List<String> strings = ListStringConverter.stringToList(reportId);
        List<String> reportIds = sampleReportIdUpdateVO.getReportIds().stream().filter(
                s -> !strings.contains(s)
        ).toList();
        strings.addAll(reportIds);
        // 设置报告文件Id，并更新
        Sample sample = new Sample();
        sample.setId(sampleReportIdUpdateVO.getSampleId());
        sample.setReportId(ListStringConverter.listToString(strings));
        this.updateById(sample);
        // 设置项目状态已完成，并设置报告id，并更新
        List<Long> sampleProjectIds = sampleReportIdUpdateVO.getSampleProjectIds();
        List<SampleProject> sampleProjects = sampleProjectIds.stream().map(sampleProjectId -> {
            SampleProject sampleProject = new SampleProject();
            sampleProject.setId(sampleProjectId);
            sampleProject.setFileId(repId);
            sampleProject.setProjectStatus(ProjectStatus.COMPLETED.getValue());
            return sampleProject;
        }).toList();
        sampleProjectService.updateBatchById(sampleProjects);
        // 样本状态更新
        this.updateSampleStatus(sampleReportIdUpdateVO.getSampleId());
        // 更新文件归属
        List<FileInfo> fileInfos = reportIds.stream().map(rId -> {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setId(rId);
            fileInfo.setSampleId(sampleReportIdUpdateVO.getSampleId());
            fileInfo.setIsUse(Boolean.TRUE);
            return fileInfo;
        }).toList();
        fileInfoService.updateBatchById(fileInfos);
    }

    @Override
    public List<? extends Object> getExportSampleInfo(LoginUser user, SampleRequestVO sampleRequestVO) {
        // 判断权限 按照不同用户查询不同信息
        QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
        // 代管样本list
        List<SampleHosting> sampleHostings = sampleHostingService.list();
        SampleHostingForSample sampleHostingForSample = new SampleHostingForSample(sampleHostings, Long.valueOf(user.getUserId()));
        // 数据权限条件
        // 1.超级管理员：查询所有样本 2.内部组：查询所有样本
        if (!user.isSuper() && !user.isInternalGroup()) {
            if (user.isGroupSuper()) {
                objectQueryWrapper.eq("user_group_id", user.getUserGroupId());
            } else {
                // 代管样本的id
                Set<Long> SampleIdList = sampleHostingForSample.getSampleHostingIdsByUserId();
                // 用户本身的样本和代管的样本
                objectQueryWrapper.and(true, wrapper -> wrapper
                        .eq("user_id", user.getUserId())
                        .or()
                        .in(!SampleIdList.isEmpty(), "s.id", SampleIdList)
                );

            }
        }
        // 设置分页数据为-1,查询所有数据
        sampleRequestVO.setPageSize(-1);
        sampleRequestVO.setPage(1);
        IPage<SampleInfoVO> sampleInfoVOIPage = this.getSampleInfoVOS2(user, sampleRequestVO, objectQueryWrapper, sampleHostingForSample);
        List<SampleInfoVO> records = sampleInfoVOIPage.getRecords();
        // 先过滤符合样本id的
        if (sampleRequestVO.getSampleIds() != null && !sampleRequestVO.getSampleIds().isEmpty()) {
            records = records.stream().filter(sampleInfoVO -> sampleRequestVO.getSampleIds().contains(sampleInfoVO.getId()))
                    .map(sampleInfoVO -> {
                        // 过滤掉 projectStatus 为 0 的项目
                        List<SampleProjectVO> filteredProjects = sampleInfoVO.getProjects().stream()
                                .filter(project -> project.getProjectStatus() != ProjectStatus.CANCELLED.getValue())
                                .collect(Collectors.toList());
                        sampleInfoVO.setProjects(filteredProjects);
                        return sampleInfoVO;
                    })
                    .toList();
        }
        // 过滤
        /**
         * 导出条件
         1. 样本角度-全部
         2. 项目角度-全部
         3. 项目角度 - 仅外送项目
         4. 项目角度 - 仅自检项目
         5. 样本角度-指定仪器
         分类：基于项目为基准
         包含字段项目状态、项目提交时间、名称、性别、年龄、项目、是否外送
         */
        int exportCondition = sampleRequestVO.getExportCondition();
        if (exportCondition == 1 || exportCondition == 5) {
            // 全部
            return records;
        } else if (exportCondition == 2 || exportCondition == 3 || exportCondition == 4) {
            List<ExportProjectSampleVO> exportProjectSampleVOS = records.stream()
                    .flatMap(sampleInfoVO -> {
                        List<SampleProjectVO> projects = sampleInfoVO.getProjects();
                        if (exportCondition == 2) {
                            return projects.stream().map(project -> {
                                ExportProjectSampleVO exportProjectSampleVO = new ExportProjectSampleVO(sampleInfoVO, project);
                                // 处理项目名称，格式化，目的导出表格美化
                                exportProjectSampleVO.setProjectName(processProjectName(exportProjectSampleVO.getProjectName()));
                                return exportProjectSampleVO;
                            });
                        } else if (exportCondition == 3) {
                            // 仅外送项目
                            return projects.stream()
                                    .filter(sampleProjectVO -> sampleProjectVO.getIsDeliveryOutside() != null && sampleProjectVO.getIsDeliveryOutside())
                                    .map(project -> {
                                        ExportProjectSampleVO exportProjectSampleVO = new ExportProjectSampleVO(sampleInfoVO, project);
                                        // 处理项目名称，格式化，目的导出表格美化
                                        exportProjectSampleVO.setProjectName(processProjectName(exportProjectSampleVO.getProjectName()));
                                        return exportProjectSampleVO;
                                    });
                        } else if (exportCondition == 4) {
                            // 仅自检项目
                            return projects.stream()
                                    .filter(sampleProjectVO -> sampleProjectVO.getIsDeliveryOutside() != null && !sampleProjectVO.getIsDeliveryOutside())
                                    .map(project -> {
                                        ExportProjectSampleVO exportProjectSampleVO = new ExportProjectSampleVO(sampleInfoVO, project);
                                        // 处理项目名称，格式化，目的导出表格美化
                                        exportProjectSampleVO.setProjectName(processProjectName(exportProjectSampleVO.getProjectName()));
                                        return exportProjectSampleVO;
                                    });
                        } /*else if (exportCondition == 5) {
                            // 筛选仪器数据，已经在查询时筛选了
//                            instrumentIds
                            return projects.stream()
                                    .map(project -> {
                                        ExportProjectSampleVO exportProjectSampleVO = new ExportProjectSampleVO(sampleInfoVO, project);
                                        // 处理项目名称，格式化，目的导出表格美化
                                        exportProjectSampleVO.setProjectName(processProjectName(exportProjectSampleVO.getProjectName()));
                                        return exportProjectSampleVO;
                                    });
                        }*/ else {
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.toList());

            return exportProjectSampleVOS;
        }
        return records;
    }

    /**
     * 处理项目名称，除性激素外，其他项目，若字符数大于18且包含（）或()的，把（）或()内的内容去除
     *
     * @param projectName
     * @return
     */
    private String processProjectName(String projectName) {
        if (!projectName.contains("性激素") && projectName.length() > 18) {
            // 使用正则表达式去除（）或()及其内部的内容
            projectName = projectName.replaceAll("\\([^)]*\\)", "").replaceAll("\\（[^）]*\\）", "");
        }
        return projectName;
    }

    @Override
    public Map<Integer, Long> summaryOfInspectionTasks(LoginUser user, SampleRequestVO sampleRequestVO) {
        // 判断权限 按照不同用户查询不同信息
        QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
        // 代管样本list
        List<SampleHosting> sampleHostings = sampleHostingService.list();
        SampleHostingForSample sampleHostingForSample = new SampleHostingForSample(sampleHostings, Long.valueOf(user.getUserId()));
        // 数据权限条件
        // 1.超级管理员：查询所有样本 2.内部组：查询所有样本
        if (!user.isSuper() && !user.isInternalGroup()) {
            if (user.isGroupSuper()) {
                objectQueryWrapper.eq("user_group_id", user.getUserGroupId());
            } else {
                // 代管样本的id
                Set<Long> SampleIdList = sampleHostingForSample.getSampleHostingIdsByUserId();
                // 用户本身的样本和代管的样本
                objectQueryWrapper.and(true, wrapper -> wrapper
                        .eq("user_id", user.getUserId())
                        .or()
                        .in(!SampleIdList.isEmpty(), "s.id", SampleIdList)
                );

            }
        }
        // 其他查询条件
        Optional.ofNullable(sampleRequestVO).ifPresent(sample -> {
            objectQueryWrapper.eq(true, "1", "1");
            objectQueryWrapper.like(StrUtil.isNotBlank(sample.getName()), "name", sample.getName());
            objectQueryWrapper.eq(StrUtil.isNotBlank(sample.getSex()), "sex", sample.getSex());
            objectQueryWrapper.eq(Objects.nonNull(sample.getAge()), "age", sample.getAge());
            objectQueryWrapper.eq(Objects.nonNull(sample.getBirthday()), "birthday", sample.getBirthday());
            objectQueryWrapper.eq(StrUtil.isNotBlank(sample.getStatus()), "status", sample.getStatus());
            objectQueryWrapper.like(StrUtil.isNotBlank(sample.getLogisticsTrackingNumber()), "logistics_tracking_number", sample.getLogisticsTrackingNumber());
            objectQueryWrapper.like(StrUtil.isNotBlank(sample.getRemarks()), "s.remarks", sample.getRemarks());
            objectQueryWrapper.eq(StrUtil.isNotBlank(sample.getSampleId()), "s.sample_id", sample.getSampleId());
            objectQueryWrapper.eq(Objects.nonNull(sample.getSampleUserGroupId()), "s.sample_user_group_id", sample.getSampleUserGroupId());
            if (sample.getSubmissionTime() != null && sample.getSubmissionTime().size() == 2) {
                objectQueryWrapper.ge("submission_time", sample.getSubmissionTime().get(0));
                objectQueryWrapper.le("submission_time", sample.getSubmissionTime().get(1));
            }
            if (sample.getUpdateTime() != null && sample.getUpdateTime().size() == 2) {
                objectQueryWrapper.ge("s.update_time", sample.getUpdateTime().get(0));
                objectQueryWrapper.le("s.update_time", sample.getUpdateTime().get(1));
            }
            // 提交时间降序排列
            objectQueryWrapper.orderBy(
                    StrUtil.isNotBlank(sampleRequestVO.getField()) && StrUtil.isNotBlank(sampleRequestVO.getOrder()),
                    Objects.equals("ascend", sampleRequestVO.getOrder()) ? true : false,
                    sampleRequestVO.getField());

        });
        // 查询非删除数据
        objectQueryWrapper.eq(true, "s.is_del", 0);
        // 必须保留，否则会报错，除非更改xml
        objectQueryWrapper.groupBy(true, "s.id");
        // 这个查询就是保持与查询list的sql一样
        IPage<Sample> samplePage = baseMapper.selectPageSamples(new Page<>(1, -1), objectQueryWrapper, null);
        List<Sample> samples = samplePage.getRecords();
        // 统计所有的status数量
        Map<Integer, Long> collect = samples.stream().collect(Collectors.groupingBy(Sample::getStatus, Collectors.counting()));

        return collect;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeSamples(DeleteSampleVO vo) {
        List<String> ids = vo.getIds();
        // 1.检查问题
        for (String id : ids) {
            List<SampleDTO> selectInfo = baseMapper.selectInfoById(Long.valueOf(id));
            // 是否存在非待定或取消
            List<SampleDTO> list = selectInfo.stream().filter(sampleProject ->
                    sampleProject.getProjectStatus() != null &&
                            sampleProject.getProjectStatus() != 0
                            && sampleProject.getProjectStatus() != 1).toList();
            if (list != null && list.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("删除失败，仅能删除所有项目为待定的样本：");

                HashMap<Long, String> existName = new HashMap<>();
                for (SampleDTO sampleDTO : list) {
                    if (existName.getOrDefault(sampleDTO.getId(), null) == null) {
                        stringBuilder.append("在姓名为《").append(sampleDTO.getName()).append("》样本。");
                    }
                    existName.put(sampleDTO.getId(), sampleDTO.getProjectName());

                    stringBuilder.append(sampleDTO.getProjectName()).append("项目的状态为“")
                            .append(ProjectStatus.getNameByValue(sampleDTO.getProjectStatus()))
                            .append("”，");
                }
                String substring = stringBuilder.substring(0, stringBuilder.length() - 1);
                throw new RuntimeException(substring);
            }
        }
        for (String id : ids) {
            //	"样本项目表"的“项目状态”字段更改  取消
//            sampleProjectService.update(new LambdaUpdateWrapper<SampleProject>().eq(SampleProject::getSampleId, id)
//                    .set(SampleProject::getProjectStatus, 0));
            // 直接逻辑删除样本项目
            sampleProjectService.remove(new LambdaUpdateWrapper<SampleProject>().eq(SampleProject::getSampleId, id));
        }
        // 修改样本状态，设置为无项目，并设置删除状态
        this.update(new LambdaUpdateWrapper<Sample>().in(Sample::getId, ids).set(Sample::getStatus, SampleStatus.NO_PROJECT.getValue())
                .set(Sample::getIsDel, true));


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<InspectionMissionUserVO> queryOtherGroupUsers(LoginUser userInfo) {
        Long userGroupId = userInfo.getUserGroupId();
        // 查询用户组下的所有用户
        List<User> list = usersService.list(new LambdaQueryWrapper<User>().eq(User::getUserGroupId, userGroupId).orderBy(true, true, User::getUsername));
        // 过滤自己 得到 所有用户名
        List<InspectionMissionUserVO> collect = list.stream().filter(user -> !user.getId().toString().equals(userInfo.getUserId())).map(user -> {
            InspectionMissionUserVO inspectionMissionUserVO = new InspectionMissionUserVO();
            inspectionMissionUserVO.setId(user.getId());
            inspectionMissionUserVO.setName(user.getUsername());
            return inspectionMissionUserVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sampleTransfer(SampleTransferVO sampleTransferVO) {
        // 修改一下样本的用户的id
        List<Sample> list = sampleTransferVO.getIds().stream().map(id -> {
            Sample sample = new Sample();
            sample.setId(Long.valueOf(id));
            sample.setUserId(sampleTransferVO.getSampleTransferId());
            return sample;
        }).toList();
        this.updateBatchById(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sampleHosting(SampleHostingVO sampleHostingVO) {
        // 查所有待管信息
        List<SampleHosting> listByUser = sampleHostingService.list(new LambdaQueryWrapper<SampleHosting>()
                .eq(SampleHosting::getHostingUserId,
                        sampleHostingVO.getSampleHostingId()));
        // 代管表新增数据
        List<SampleHosting> list = sampleHostingVO.getIds().stream().map(id -> {
            SampleHosting sampleHosting = new SampleHosting();
            sampleHosting.setSampleId(Long.valueOf(id));
            sampleHosting.setHostingUserId(sampleHostingVO.getSampleHostingId());
            return sampleHosting;
        }).filter(sampleHosting -> {
            // 过滤相同样本id的数据，避免重复代管, 避免重复添加内容在数据库
            return !listByUser.stream().anyMatch(sampleHosting1 -> sampleHosting1.getSampleId().equals(sampleHosting.getSampleId()));
        }).toList();
        sampleHostingService.saveBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void noHosting(String username, NoHostingVO noHostingVO) {
        // 发送通知
        messageNotificationService.sendNotificationsWhenCancelSampleCustody(username, noHostingVO.getIds());
        List<String> ids = noHostingVO.getIds();
        // 删除代管表数据
        sampleHostingService.remove(new LambdaQueryWrapper<SampleHosting>().in(SampleHosting::getSampleId, ids));
    }

    @Override
    public void saveSampleInfo(List<SampleInfoSheetData.SampleInfo> dataSource, LoginUser userInfo) {
        List<Sample> list = dataSource.stream().map(sampleInfoSheetData -> {
            Sample sample = new Sample();
            BeanUtils.copyProperties(sampleInfoSheetData, sample);
            sample.setUserId(Long.valueOf(userInfo.getUserId()));
            sample.setUserGroupId(userInfo.getUserGroupId());
            sample.setBirthday(LocalDate.parse(sampleInfoSheetData.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            sample.setSubmissionTime(LocalDateTime.now());
            sample.setStatus(0);
            return sample;
        }).toList();
//        System.out.println(list);
        this.saveBatch(list);
    }

    @Override
    public Collection<InspectionMissionUserVO> queryOtherGroupUsersCascader(LoginUser userInfo) {
        List<GroupUsersCascaderDTO> groupUsersCascaderDTOS = usersMapper.selectGroupUsersCascader();
        // 排除当前用户
        groupUsersCascaderDTOS = groupUsersCascaderDTOS.stream()
                .filter(groupUsersCascaderDTO -> !groupUsersCascaderDTO.getUserId().equals(Long.valueOf(userInfo.getUserId())))
                .toList();
        HashMap<Long, InspectionMissionUserVO> root = new HashMap<>();
        for (GroupUsersCascaderDTO groupUsersCascaderDTO : groupUsersCascaderDTOS) {
            if (root.getOrDefault(groupUsersCascaderDTO.getUserGroupId(), null) != null) {
                InspectionMissionUserVO inspectionMissionUserVO = root.get(groupUsersCascaderDTO.getUserGroupId());
                List<InspectionMissionUserVO> children = inspectionMissionUserVO.getChildren();
                InspectionMissionUserVO userVO = new InspectionMissionUserVO();
                userVO.setName(groupUsersCascaderDTO.getUsername());
                userVO.setId(groupUsersCascaderDTO.getUserId());
                children.add(userVO);
            } else {
                InspectionMissionUserVO inspectionMissionUserVO = new InspectionMissionUserVO();
                inspectionMissionUserVO.setName(groupUsersCascaderDTO.getGroupName());
                inspectionMissionUserVO.setId(groupUsersCascaderDTO.getUserGroupId());
                inspectionMissionUserVO.setChildren(new ArrayList<>());
                root.put(groupUsersCascaderDTO.getUserGroupId(), inspectionMissionUserVO);
                if (groupUsersCascaderDTO.getUserId() != null) {
                    InspectionMissionUserVO v2 = new InspectionMissionUserVO();
                    v2.setName(groupUsersCascaderDTO.getUsername());
                    v2.setId(groupUsersCascaderDTO.getUserId());
                    inspectionMissionUserVO.getChildren().add(v2);
                }
            }

        }
//        System.out.println(groupUsersCascaderDTOS);
        return root.values();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSample(SampleUpdateVO sampleUpdateVO) {
        // 1.更新样本信息
        Sample sample = new Sample();
        BeanUtils.copyProperties(sampleUpdateVO, sample);
        this.updateById(sample);
        // 2.更新项目信息
        // 获取原本拥有的
        List<SampleProject> list = sampleProjectService.list(new QueryWrapper<SampleProject>().eq("sample_id", sampleUpdateVO.getId()));
        Map<Long, List<SampleProject>> listMap = list.stream().collect(Collectors.groupingBy(SampleProject::getProjectId));
        // 项目的新增
        Optional.ofNullable(sampleUpdateVO.getProjects()).filter(projects -> projects.size() > 0)
                .ifPresent(projects -> {
                    // 新增当前
                    List<SampleProject> sampleProjects = projects.stream()
                            // 过滤掉原本有的
                            .filter(project -> !listMap.containsKey(project.getId()))
                            .map(project -> {
                                SampleProject sampleProject = new SampleProject();
                                sampleProject.setSampleId(sampleUpdateVO.getId());
                                sampleProject.setProjectId(project.getId());
                                sampleProject.setProjectStatus(project.getStatus());
                                return sampleProject;
                            }).toList();
                    sampleProjectService.saveBatch(sampleProjects);
                });
        // 更新样本状态 （复用）
        this.updateSampleStatus(sampleUpdateVO.getId());

    }

    @Override
    public void updateSampleStatus(Long sampleId) {
        // 1.查出项目信息
        QueryWrapper<SampleProject> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("project_status");
        objectQueryWrapper.eq("sample_id", sampleId);
//        List<ProjectManageDTO> projectManageDTOS = sampleProjectMapper.selectProjects(objectQueryWrapper);
        List<SampleProject> sampleProjects = sampleProjectMapper.selectList(objectQueryWrapper);
        // 2.判断修改样本信息
        int size = OptionalInt.of(sampleProjects.size()).orElse(0);
        Map<Integer, Long> statusMap = sampleProjects.stream().collect(Collectors.groupingBy(projects -> projects.getProjectStatus(), Collectors.counting()));
        Integer sampleStatus = this.getSampleStatus(size, statusMap);
        // 3.更新样本状态
        Sample sample = new Sample();
        sample.setStatus(sampleStatus);
        sample.setId(sampleId);
        this.updateById(sample);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IPage<SampleInfoVO> getEscrowSampleInfo(LoginUser user, SampleRequestVO sampleRequestVO) {
        // 判断权限 按照不同用户查询不同信息
        QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
        // 代管样本list
        List<SampleHosting> sampleHostings = sampleHostingService.list();
        SampleHostingForSample sampleHostingForSample = new SampleHostingForSample(sampleHostings, Long.valueOf(user.getUserId()));
        // 数据权限条件
        if (!user.isSuper() && !user.isInternalGroup()) {
            if (user.isGroupSuper()) {
                objectQueryWrapper.eq("user_group_id", user.getUserGroupId());
            } else {
                // 代管样本的id
                Set<Long> SampleIdList = sampleHostingForSample.getSampleHostingIdsByUserId();
                // 用户本身的样本和代管的样本
                objectQueryWrapper.and(true, wrapper -> wrapper
                        .eq("user_id", user.getUserId())
                        .or()
                        .in(!SampleIdList.isEmpty(), "s.id", SampleIdList)
                );

            }
        }
        // 保证是在代管表中样本，确认是查询代管的样本和被代管的样本
        Set<Long> sampleHostingsIds = sampleHostingForSample.getSampleHostingsIds();
        // 不存在直接空
        if (sampleHostingsIds == null || sampleHostingsIds.isEmpty()) {
            // 创建一个新的 IPage<SampleInfoVO> 对象
            IPage<SampleInfoVO> sampleInfoVOIPage = new Page<>();
            sampleInfoVOIPage.setRecords(new ArrayList<>());
            sampleInfoVOIPage.setCurrent(1); // 设置当前页为1
            sampleInfoVOIPage.setSize(0); // 设置每页大小为0
            sampleInfoVOIPage.setTotal(0L); // 设置总记录数为0
            sampleInfoVOIPage.setPages(0); // 设置总页数为0
            return sampleInfoVOIPage;
        }
        objectQueryWrapper.in("s.id", sampleHostingForSample.getSampleHostingsIds());
        IPage<SampleInfoVO> sampleInfoVOIPage = this.getSampleInfoVOS2(user, sampleRequestVO, objectQueryWrapper, sampleHostingForSample);
        return sampleInfoVOIPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject getLogisticsInformation(GetLogisticsInformationVO logisticsInformationVO) {
        QueryLogisticsInformation1 queryLogisticsInformation1 = new QueryLogisticsInformation1();
        JSONObject entries = queryLogisticsInformation1.queryLogisticsInformation(logisticsInformationVO.getLogisticsTrackingNumber(), logisticsInformationVO.getPhoneNumLastFour());
        if (entries == null) {
            // 重试一次, http请求层面和接口内容无关
            entries = queryLogisticsInformation1.queryLogisticsInformation(logisticsInformationVO.getLogisticsTrackingNumber(), logisticsInformationVO.getPhoneNumLastFour());
        }
        // TODO 构建返回格式
        return entries;
    }

    @Override
    public Collection<InspectionMissionUserVO> queryGroupUsersCascader(LoginUser userInfo) {
        List<GroupUsersCascaderDTO> groupUsersCascaderDTOS = usersMapper.selectGroupUsersCascader();
        // 构建树结构
        HashMap<Long, InspectionMissionUserVO> root = new HashMap<>();
        for (GroupUsersCascaderDTO groupUsersCascaderDTO : groupUsersCascaderDTOS) {
            if (root.getOrDefault(groupUsersCascaderDTO.getUserGroupId(), null) != null) {
                InspectionMissionUserVO inspectionMissionUserVO = root.get(groupUsersCascaderDTO.getUserGroupId());
                List<InspectionMissionUserVO> children = inspectionMissionUserVO.getChildren();
                InspectionMissionUserVO userVO = new InspectionMissionUserVO();
                // 设置用户名修改成真实姓名，如果没有真实姓名，那么设置用户名
                String realName = groupUsersCascaderDTO.getRealName();
                if (realName != null && !realName.isEmpty()) {
                    userVO.setName(realName);
                } else {
                    userVO.setName(groupUsersCascaderDTO.getUsername());
                }
                userVO.setId(groupUsersCascaderDTO.getUserId());
                children.add(userVO);
            } else {
                InspectionMissionUserVO inspectionMissionUserVO = new InspectionMissionUserVO();
                inspectionMissionUserVO.setName(groupUsersCascaderDTO.getGroupName());
                inspectionMissionUserVO.setId(groupUsersCascaderDTO.getUserGroupId());
                inspectionMissionUserVO.setChildren(new ArrayList<>());
                root.put(groupUsersCascaderDTO.getUserGroupId(), inspectionMissionUserVO);
                if (groupUsersCascaderDTO.getUserId() != null) {
                    InspectionMissionUserVO v2 = new InspectionMissionUserVO();
                    // 设置用户名修改成真实姓名，如果没有真实姓名，那么设置用户名
                    String realName = groupUsersCascaderDTO.getRealName();
                    if (realName != null && !realName.isEmpty()) {
                        v2.setName(realName);
                    } else {
                        v2.setName(groupUsersCascaderDTO.getUsername());
                    }
                    v2.setId(groupUsersCascaderDTO.getUserId());
                    inspectionMissionUserVO.getChildren().add(v2);
                }
            }

        }
        return root.values();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeSampleProjectsStatus(ChangeSampleProjectsVO sampleProjectsVO) {
        Integer status = sampleProjectsVO.getStatus();
        for (Long sampleProjectId : sampleProjectsVO.getSampleProjectIds()) {
            SampleProject sampleProject = new SampleProject();
            sampleProject.setId(sampleProjectId);
            sampleProject.setProjectStatus(status);
            sampleProjectService.updateById(sampleProject);
        }
        List<SampleProject> list = sampleProjectService.list(new LambdaQueryWrapper<SampleProject>().in(SampleProject::getId, sampleProjectsVO.getSampleProjectIds()));
        Set<Long> collected = list.stream().map(SampleProject::getSampleId).collect(Collectors.toSet());
        // 更新样本状态
        for (Long sampleId : collected) {
            this.updateSampleStatus(sampleId);
        }
    }
}

@Data
class SampleHostingForSample {

    private List<SampleHosting> sampleHostings;
    /**
     * 所有代管样本id
     */
    private Set<Long> sampleHostingsIds;

    /**
     * 某用户代管的样本id
     */
    private Set<Long> sampleHostingIdsByUserId;

    /**
     * 被某用户代管的样本id
     */
    private Set<Long> beSampleHostingIdsByUserId;

    /**
     * @param sampleHostings
     * @param userId
     */
    public SampleHostingForSample(List<SampleHosting> sampleHostings, Long userId) {
        this.sampleHostings = sampleHostings;
        this.sampleHostingsIds = sampleHostings.stream().map(SampleHosting::getSampleId).collect(Collectors.toSet());
        this.sampleHostingIdsByUserId = this.getSampleHostingsByUserId(userId);
        this.beSampleHostingIdsByUserId = this.getBeSampleHostingIds();
    }


    /**
     * 通过uerid获取代管的样本ids
     *
     * @param userId
     * @return
     */
    private Set<Long> getSampleHostingsByUserId(Long userId) {
        Set<Long> sampleIdSet = sampleHostings.stream().filter(sampleHosting -> sampleHosting.getHostingUserId().equals(userId))
                .map(sampleHosting -> sampleHosting.getSampleId()).collect(Collectors.toSet());
        return sampleIdSet;
    }

    /**
     * 获取被代管样本的id
     * 所有样本表中除掉 【代管的样本id】之后剩下的样本
     *
     * @return
     */
    private Set<Long> getBeSampleHostingIds() {
        Set<Long> userIdsSet = new HashSet<>(sampleHostingIdsByUserId);
        Set<Long> result = sampleHostingsIds.stream()
                .filter(id -> !userIdsSet.contains(id))
                .collect(Collectors.toSet());
        return result;
    }


}