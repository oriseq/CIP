package com.oriseq.controller.sample;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.common.utils.ListStringConverter;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectManageDTO;
import com.oriseq.dtm.entity.*;
import com.oriseq.dtm.enums.ProjectStatus;
import com.oriseq.dtm.vo.*;
import com.oriseq.dtm.vo.sample.*;
import com.oriseq.service.*;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 样本管理
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/30 11:19
 */
@RestController
@RequestMapping("sample")
@EnableLogging
@Slf4j
public class InspectionMissionController {


    @Autowired
    private LoginTool loginTool;

    @Autowired
    private ISampleService sampleService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private DeliveryUnitService deliveryUnitService;

    @Autowired
    private MessageNotificationService messageNotificationService;

    @Autowired
    private ISampleProjectService sampleProjectService;

    @Autowired
    private IFileInfoService fileInfoService;

    @Autowired
    private InstrumentService instrumentService;

    /**
     * 获取送检样本信息
     *
     * @return
     */
    @PostMapping("sampleInformation")
    public Result<IPage<SampleInfoVO>> sampleInformation(
            @RequestBody(required = false) SampleRequestVO sampleRequestVO,
            HttpServletRequest request) {
        LoginUser user = loginTool.getUserInfo(request);
        IPage<SampleInfoVO> sampleInfoVOIPage = sampleService.getSampleInfo(user, sampleRequestVO);
        return Result.defaultSuccessByMessageAndData("获取成功", sampleInfoVOIPage);
    }


    /**
     * 导出送检样本信息
     *
     * @param sampleRequestVO
     * @param request
     * @return
     */
    @PostMapping("exportSampleInformation")
    public Result<List<? extends Object>> exportSampleInformation(
            @RequestBody(required = false) SampleRequestVO sampleRequestVO,
            HttpServletRequest request) {
        LoginUser user = loginTool.getUserInfo(request);
        List<? extends Object> exportSampleInfo = sampleService.getExportSampleInfo(user, sampleRequestVO);
        return Result.defaultSuccessByMessageAndData("获取成功", exportSampleInfo);
    }

    /**
     * 送检任务汇总 统计不同状态的数量
     *
     * @return
     */
    @PostMapping("summaryOfInspectionTasks")
    public Result<Map<Integer, Long>> summaryOfInspectionTasks(
            @RequestBody(required = false) SampleRequestVO sampleRequestVO) {
        LoginUser user = loginTool.getUserInfo();
        Map<Integer, Long> integerLongMap = sampleService.summaryOfInspectionTasks(user, sampleRequestVO);
        return Result.defaultSuccessByMessageAndData("获取成功", integerLongMap);
    }


    /**
     * 删除样本信息
     *
     * @return
     */
    @PutMapping("sampleInformation")
    public Result<ArrayList<String>> deleteSampleInformation(@RequestBody DeleteSampleVO deleteSampleVO, HttpServletRequest request) {
//        Object attribute = RequestContextHolder.getRequestAttributes().getAttribute("Authorization", RequestAttributes.SCOPE_REQUEST);
//        LoginUser userInfo = loginTool.getUserInfo(request);
        // 修改项目状态
        sampleService.changeSamples(deleteSampleVO);
        return Result.defaultSuccessByMessage("删除成功");
    }


    /**
     * 用户查询分组其他用户
     *
     * @return
     */
    @GetMapping("otherGroupUsers")
    public Result<List<InspectionMissionUserVO>> otherGroupUsers(HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        List<InspectionMissionUserVO> inspectionMissionUserVOS = sampleService.queryOtherGroupUsers(userInfo);
        return Result.defaultSuccessByMessageAndData("查询成功", inspectionMissionUserVOS);
    }

    /**
     * 查询所有分租下用户, 排除自己
     *
     * @return
     */
    @GetMapping("otherGroupUsersCascader")
    public Result<Collection<InspectionMissionUserVO>> otherGroupUsersCascader(HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        Collection<InspectionMissionUserVO> inspectionMissionUserVOS = sampleService.queryOtherGroupUsersCascader(userInfo);
//        String json = new Gson().toJson(inspectionMissionUserVOS);
        return Result.defaultSuccessByMessageAndData("查询成功", inspectionMissionUserVOS);
    }

    /**
     * 查询所有分租下用户
     *
     * @return
     */
    @GetMapping("groupUsersCascader")
    public Result<Collection<InspectionMissionUserVO>> groupUsersCascader() {
        LoginUser userInfo = loginTool.getUserInfo();
        Collection<InspectionMissionUserVO> inspectionMissionUserVOS = sampleService.queryGroupUsersCascader(userInfo);
//        String json = new Gson().toJson(inspectionMissionUserVOS);
        return Result.defaultSuccessByMessageAndData("查询成功", inspectionMissionUserVOS);
    }

    /**
     * 样本过户
     *
     * @return
     */
    @PutMapping("sampleTransfer")
    public Result<ArrayList<String>> sampleTransfer(@Validated @RequestBody SampleTransferVO sampleTransferVO, HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // 确保样本所属用户组和选择的代管用户所属用户组一致
        User byId = usersService.getById(sampleTransferVO.getSampleTransferId());
        Long userGroupId = byId.getUserGroupId();
        boolean b = sampleTransferVO.getIds().stream().map(id -> {
            Sample byId1 = sampleService.getById(id);
            Long userGroupId1 = byId1.getUserGroupId();
            return userGroupId1;
        }).allMatch(userGroupId1 -> userGroupId1.equals(userGroupId));
        if (!b) {
            return Result.defaultErrorByMessage("请确保样本所属用户组和选择的代管用户所属用户组一致，请重新选择");
        }
        sampleService.sampleTransfer(sampleTransferVO);
        // 发送通知
        messageNotificationService.sendNotificationsWhenSampleTransfer(sampleTransferVO.getSampleTransferId(),
                StrUtil.isNotBlank(userInfo.getRealName()) ? userInfo.getRealName() : userInfo.getUsername()
                , sampleTransferVO.getIds());
        return Result.defaultSuccessByMessage("过户成功");
    }

    /**
     * 样本代管
     *
     * @return
     */
    @PostMapping("sampleHosting")
    public Result<ArrayList<String>> sampleHosting(@Validated @RequestBody SampleHostingVO sampleHostingVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        // 样本的所属用户组和代管用户所属组是否同一组
        // 1.代管用户所属组
        Long sampleHostingId = sampleHostingVO.getSampleHostingId();
        User byId = usersService.getById(sampleHostingId);
        Long userGroupId = byId.getUserGroupId();
        // 2.样本组
        boolean b = sampleHostingVO.getIds().stream().map(id -> {
            Sample byId1 = sampleService.getById(id);
            Long userGroupId1 = byId1.getUserGroupId();
            return userGroupId1;
        }).allMatch(userGroupId1 -> userGroupId1.equals(userGroupId));
        if (!b) {
            return Result.defaultErrorByMessage("请确保样本所属用户组和选择的代管用户所属用户组一致，请重新选择");
        }
        sampleService.sampleHosting(sampleHostingVO);
        // 发送通知
        messageNotificationService.sendNotificationsWhenSampleCustody(sampleHostingId,
                StrUtil.isNotBlank(userInfo.getRealName()) ? userInfo.getRealName() : userInfo.getUsername()
                , sampleHostingVO.getIds());
        return Result.defaultSuccessByMessage("代管成功");
    }

    /**
     * 取消代管
     *
     * @return
     */
    @PostMapping("noHosting")
    public Result noHosting(@Validated @RequestBody NoHostingVO noHostingVO, HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        sampleService.noHosting(StrUtil.isNotBlank(userInfo.getRealName()) ? userInfo.getRealName() : userInfo.getUsername(), noHostingVO);
        return Result.defaultSuccessByMessage("取消代管成功");
    }


    /**
     * 获取所有项目
     *
     * @return
     */
    @GetMapping("projectList")
    public Result<List<Project>> getProjectList(HttpServletRequest request) throws JsonProcessingException {
        LoginUser userInfo = loginTool.getUserInfo(request);
        List<Project> projects = projectService.getProjects(userInfo);
        if (projects == null && projects.size() == 0) {
            throw new RuntimeException("项目信息不存在");
        }
        return Result.defaultSuccessByMessageAndData("获取项目成功", projects);
    }


    /**
     * 导入样本信息
     *
     * @return
     */
    @PostMapping("importSample")
    public Result<SampleInfoSheetData> importSample(@Validated @RequestBody SampleInfoSheetData sampleTransferVO, HttpServletRequest request) {

        LoginUser userInfo = loginTool.getUserInfo(request);
//        sampleService.sampleHosting(sampleTransferVO);
        // 1.校验
        List<SampleInfoSheetData.SampleInfo> dataSource = sampleTransferVO.getDataSource();
        int index = 2;
        for (SampleInfoSheetData.SampleInfo sampleInfo : dataSource) {
            boolean isException = false;
            StringBuilder exceptionStr = new StringBuilder();
            if (StrUtil.isBlank(sampleInfo.getName())) {
                isException = true;
                exceptionStr.append("第" + index + "行<<姓名>>不能为空").append(",");
            }
            if (Objects.isNull(sampleInfo.getSex())) {
                isException = true;
                exceptionStr.append("第" + index + "行<<性别>>不能为空").append(",");
            }
            if (Objects.isNull(sampleInfo.getAge()) || sampleInfo.getAge() <= 0) {
                isException = true;
                exceptionStr.append("第" + index + "行<<年龄>>不能为空或<=0").append(",");
            }
            if (StrUtil.isBlank(sampleInfo.getBirthday())) {
                isException = true;
                exceptionStr.append("第" + index + "行<<出生日期>>不能为空").append(",");
            } else {
                try {
                    LocalDate.parse(sampleInfo.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    exceptionStr.append("第" + index + "行<<出生日期>>的格式需要为：年/月/日，比如2021/01/01").append(",");
//                throw new RuntimeException(e);
                }
            }
            // 项目
//            if (StrUtil.isNotBlank(sampleInfo.getProjects())) {
//
//            }

            if (isException) {
                throw new RuntimeException(exceptionStr.substring(0, exceptionStr.length() - 1));
            }
            index++;
        }

        // 2.保存
        sampleService.saveSampleInfo(dataSource, userInfo);
        return Result.defaultSuccessByMessage("导入成功");
    }

    /**
     * 修改样本信息
     *
     * @return
     */
    @PutMapping("sampleInfo")
    public Result<SampleInfoSheetData> updateSampleInformation(@Validated @RequestBody SampleUpdateVO sampleUpdateVO, HttpServletRequest request) {
//        LoginUser userInfo = loginTool.getUserInfo(request);
        sampleService.updateSample(sampleUpdateVO);
        return Result.defaultSuccessByMessage("更新成功");
    }


    /**
     * 获取物流信息
     *
     * @return
     */
    @PostMapping("logisticsInformation")
    public Result<JSONArray> getLogisticsInformation(@Validated @RequestBody GetLogisticsInformationVO logisticsInformationVO) {
        JSONObject logisticsInformation1 = sampleService.getLogisticsInformation(logisticsInformationVO);
        if (logisticsInformation1 == null) {
            return Result.defaultErrorByMessage("物流信息不存在");
        }
        /*
            1: message:查询不到信息，请核对单号和快递公司是否正确
            2: message:请输入【收件人】或【寄件人】\n电话号码后4位
        * */
        String errCode = logisticsInformation1.get("errCode").toString();
        if ("1".equals(errCode)) {
            return Result.defaultErrorByMessage("查询不到信息，请核对单号和快递公司是否正确");
        } else if ("2".equals(errCode)) {
            return Result.defaultErrorByMessage("请输入收件人或寄件人电话号码后4位");
        }
        JSONArray jsonArray = logisticsInformation1.getJSONArray("data");
        return Result.defaultSuccessByMessageAndData("获取物流信息成功", jsonArray);
    }

    /**
     * 用于选择器的外送单位
     *
     * @return
     */
    @GetMapping("deliveryUnitSelect")
    public Result<List<JSON>> deliveryUnitSelect(@RequestParam(value = "projectId", required = false) Long projectId) {
        List<Long> idList = null;
        if (!Objects.isNull(projectId)) {
            Project byId = projectService.getById(projectId);
            JSONArray deliveryUnitIds = byId.getDeliveryUnitIds();
            if (deliveryUnitIds != null && deliveryUnitIds.size() > 0) {
                idList = deliveryUnitIds.toList(Long.class);
            } else {
                return Result.defaultSuccessByMessageAndData("查询成功", Collections.emptyList());
            }
        }
        LambdaQueryWrapper<DeliveryUnit> deliveryUnitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DeliveryUnit> select = deliveryUnitLambdaQueryWrapper.select(DeliveryUnit::getName, DeliveryUnit::getId);
        deliveryUnitLambdaQueryWrapper.in(idList != null, DeliveryUnit::getId, idList);
        List<DeliveryUnit> list = deliveryUnitService.list(select);
        List<JSON> jsonList = list.stream().map(deliveryUnit -> {
            JSON duJson = JSONUtil.parse(deliveryUnit);
            return duJson;
        }).toList();
        return Result.defaultSuccessByMessageAndData("查询成功", jsonList);
    }


    /**
     * 获取样本项目选择列表
     * <p>
     * 该方法通过GET请求处理/sampleProjectSelect路径，返回一个样本对象列表
     * 主要用于提供一个接口，让前端可以获取到样本项目的选择信息
     *
     * @return 返回一个Result对象，其中包含样本列表信息
     */
    @PostMapping("sampleProjectSelect")
    public Result<List<JSONObject>> getSampleProjectSelect(@Validated @RequestBody SampleProjectSelectVO sampleProjectSelectVO) {
        List<Long> sampleIds = sampleProjectSelectVO.getSampleIds();
        List<ProjectManageDTO> projectManageDTOS = sampleProjectService.sampleProjects(sampleIds, sampleProjectSelectVO.getScene());

        //如果sampleProjectSelectVO.getScene()是uploadReports。改为级联的数据结构第一层为仪器，第二层为项目
        if (StrUtil.isNotBlank(sampleProjectSelectVO.getScene()) && sampleProjectSelectVO.getScene().equals("updateProjectStatusByInstrument")) {
            // 先把projectManageDTOS统计一下，按项目名统计，不同状态的数量，所包含的sampleProjectId
            Map<Long, Map<String, Long>> countByStatusMap = projectManageDTOS.stream()
                    .collect(Collectors.groupingBy(
                            ProjectManageDTO::getProjectId,
                            Collectors.groupingBy(dto -> dto.getProjectStatus().toString(), Collectors.counting())
                    ));

            Map<Long, List<Long>> sampleProjectIdsMap = projectManageDTOS.stream()
                    .collect(Collectors.groupingBy(
                            ProjectManageDTO::getProjectId,
                            Collectors.mapping(ProjectManageDTO::getSampleProjectId, Collectors.toList())
                    ));

            // 构建结果列表，使用 projectName 作为字段值
            List<JSONObject> result = new ArrayList<>();
            for (Map.Entry<Long, Map<String, Long>> entry : countByStatusMap.entrySet()) {
                Long projectId = entry.getKey();
                Map<String, Long> statusCount = entry.getValue();
                List<Long> sampleProjectIds = sampleProjectIdsMap.getOrDefault(projectId, Collections.emptyList());

                // 获取项目名称（假设只有一个 DTO 对应该 projectId）
                Optional<ProjectManageDTO> first = projectManageDTOS.stream()
                        .filter(dto -> dto.getProjectId() != null && dto.getProjectId().equals(projectId))
                        .findFirst();

                JSONObject obj = new JSONObject();
                obj.set("projectName", first.get().getProjectName());
                obj.set("countByStatus", statusCount);
                obj.set("sampleProjectIds", sampleProjectIds);
                obj.set("instrumentId", first.get().getInstrumentId());

                result.add(obj);
            }
            // 按照 instrument_id 分组
            Map<Long, List<JSONObject>> groupedByInstrument = result.stream()
                    .collect(Collectors.groupingBy(dto -> Optional.ofNullable((Long) dto.get("instrumentId")).orElse(-1L)));

            // 获取仪器名称（可从 Project 表或 Instrument 表中获取）
            List<Instrument> instruments = instrumentService.listByIds(groupedByInstrument.keySet());
            // 加上未分配仪器
            instruments.add(new Instrument(-1L, "未分配仪器"));

            // 构建 JSON 级联结构
            List<JSONObject> cascadeList = new ArrayList<>();
            // 添加有 instrument_id 的仪器节点
            for (Instrument instrument : instruments) {
                Long instrumentId = instrument.getId();
                String instrumentName = instrument.getName();

                List<JSONObject> projects = groupedByInstrument.get(instrumentId);
                if (projects == null || projects.isEmpty()) continue;

                JSONObject instrumentNode = new JSONObject();
                instrumentNode.set("instrumentId", instrumentId);
                instrumentNode.set("instrumentName", instrumentName);

                JSONArray children = projects.stream().collect(Collectors.toCollection(JSONArray::new));

                instrumentNode.set("children", children);
                cascadeList.add(instrumentNode);
            }

            return Result.defaultSuccessByMessageAndData("查询成功", cascadeList);
        }
        // 其他情况
        // 取sampleProjectId,和projectName构建下拉数据
        List<JSONObject> jsonObjectList = projectManageDTOS.stream().map(projectManageDTO -> {
            JSONObject entries = new JSONObject();
            entries.set("sampleProjectId", projectManageDTO.getSampleProjectId());
            entries.set("projectName", projectManageDTO.getProjectName());
            entries.set("projectStatus", projectManageDTO.getProjectStatus());
            return entries;
        }).toList();
        return Result.defaultSuccessByMessageAndData("查询成功", jsonObjectList);
    }


    /**
     * 修改样本项目状态
     *
     * @param sampleProjectsVO
     * @return
     */
    @PostMapping("changeSampleProjectsStatus")
    public Result<List<JSONObject>> changeSampleProjectsStatus(@Validated @RequestBody ChangeSampleProjectsVO sampleProjectsVO) {
        Integer status = sampleProjectsVO.getStatus();
        String nameByValue = ProjectStatus.getNameByValue(status);
        if (StringUtils.isBlank(nameByValue)) {
            return Result.defaultErrorByMessage("设置的状态不存在");
        }
        sampleService.changeSampleProjectsStatus(sampleProjectsVO);

        return Result.defaultSuccessByMessage("操作成功");
    }

    /**
     * 获取文件列表,附件或者样本报告
     *
     * @param sampleId
     * @param fileType 文件类型：1：附件 2：样本报告
     * @return
     */
    @GetMapping("fileList")
    public Result<List<FileInfoVO>> getAppendixFileList(@RequestParam String fileType, @RequestParam Long sampleId) {
        // 校验 fileType 是否合法
        if (!"1".equals(fileType) && !"2".equals(fileType)) {
            return Result.defaultSuccessByMessageAndData("查询成功", new ArrayList<>());
        }

        // 获取文件信息列表
        List<FileInfo> fileInfoList = fileInfoService.list(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getSampleId, sampleId));

        // 获取样本信息
        Sample sample = sampleService.getById(sampleId);
        if (sample == null) {
            return Result.defaultSuccessByMessageAndData("查询成功", new ArrayList<>());
        }

        // 获取文件 ID 列表
        String fileIds = "1".equals(fileType) ? sample.getAnnexFileId() : sample.getReportId();
        if (StringUtils.isBlank(fileIds)) {
            return Result.defaultSuccessByMessageAndData("查询成功", new ArrayList<>());
        }
        // 处理文件 ID 列表
        List<String> fileIdList = Arrays.stream(fileIds.split(",")).filter(StringUtils::isNotBlank).toList();

        // 根据 fileType 处理逻辑
        if ("2".equals(fileType)) {
            return handleReportFileType(fileInfoList, fileIdList, sampleId);
        } else {
            return handleAnnexFileType(fileInfoList, fileIdList);
        }
    }

    private Result<List<FileInfoVO>> handleReportFileType(List<FileInfo> fileInfoList, List<String> fileIdList, Long sampleId) {
        // 查询项目管理信息
        // 校验 fileIdList 的合法性
        if (fileIdList == null || fileIdList.isEmpty()) {
            return Result.defaultSuccessByMessageAndData("查询成功", new ArrayList<>());
        }
        // 使用参数化查询，避免 SQL 注入
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sp.sample_id", sampleId).in("file_id", fileIdList);
        // 查询未删除数据
        queryWrapper.eq("is_del", 0);
        List<ProjectManageDTO> projectManageDTOS = sampleProjectService.listProjectList(queryWrapper);

        // 构建文件 ID 对应的项目名称映射
        Map<String, List<String>> idForNameMap = projectManageDTOS.stream()
                .collect(Collectors.groupingBy(
                        ProjectManageDTO::getFileId,
                        Collectors.mapping(ProjectManageDTO::getProjectName, Collectors.toList())
                ));

        // 转换为 JSON 对象列表
        return convertToFileJsonList(fileInfoList, fileIdList, idForNameMap);
    }

    private Result<List<FileInfoVO>> handleAnnexFileType(List<FileInfo> fileInfoList, List<String> fileIdList) {
        // 转换为 JSON 对象列表
        return convertToFileJsonList(fileInfoList, fileIdList, Collections.emptyMap());
    }

    private Result<List<FileInfoVO>> convertToFileJsonList(List<FileInfo> fileInfoList, List<String> fileIdList, Map<String, List<String>> idForNameMap) {
        List<FileInfoVO> fileInfoVOList = fileInfoList.stream()
                .filter(fileInfo -> fileIdList.contains(fileInfo.getId().toString()))
                .map(fileInfo -> {
                    FileInfoVO fileInfoVO = new FileInfoVO();
                    BeanUtils.copyProperties(fileInfo, fileInfoVO);
                    fileInfoVO.setProjectNames(idForNameMap.getOrDefault(fileInfo.getId().toString(), Collections.emptyList()));
                    return fileInfoVO;
                })
                .toList();

        return Result.defaultSuccessByMessageAndData("查询成功", fileInfoVOList);
    }


    /**
     * 附件
     * 删除附件文件
     */
    @DeleteMapping("appendixFile")
    public Result<String> deleteAnnexFile(@RequestBody Map<String, String> stringMap) {
        String fileId = stringMap.get("fileId");
        FileInfo byId1 = fileInfoService.getById(fileId);
        Long sampleId = byId1.getSampleId();
        if (sampleId != null) {
            fileInfoService.deleteAnnexFile(sampleId, fileId);
        }
        return Result.defaultSuccessByMessage("删除成功");
    }

    /**
     * 附件
     * 新增附件文件
     */
    @PostMapping("appendixFile")
    public Result<String> addAnnexFile(@Validated @RequestBody AddAnnexFileVO addAnnexFileVO) {
        // 1. 获取样本信息
        Sample sample = sampleService.getById(addAnnexFileVO.getSampleId());
        if (sample == null) {
            return Result.defaultErrorByMessage("样本不存在");
        }
        // 判断附件列大小
        String annexFileId = sample.getAnnexFileId();
        List<String> strings = ListStringConverter.stringToList(annexFileId);
        // 一个样本不超过10个附件
        if (strings.size() >= 10) {
            return Result.defaultErrorByMessage("附件数量已达上限");
        }
        // 新增附件
        fileInfoService.addAnnexFile(addAnnexFileVO.getFileId(), addAnnexFileVO.getSampleId());
        // 4. 返回结果
        return Result.defaultSuccessByMessage("附件添加成功");
    }

    /**
     * 报告
     * 删除报告文件
     */
    @DeleteMapping("reportFile")
    public Result<String> deleteReportFile(@RequestBody Map<String, String> stringMap) {
        String fileId = stringMap.get("fileId");
        FileInfo byId1 = fileInfoService.getById(fileId);
        if (byId1 == null) {
            return Result.defaultErrorByMessage("文件不存在");
        }
        Long sampleId = byId1.getSampleId();
        if (sampleId != null) {
            fileInfoService.deleteReportFile(sampleId, fileId);
        } else {
            fileInfoService.deleteFile(fileId);
        }
        return Result.defaultSuccessByMessage("删除成功");
    }


    /**
     * 新增报告
     * 修改样本报告文件Id（新增上传报告），并修改项目状态为完成
     *
     * @return
     */
    @PutMapping("sampleReportId")
    public Result<SampleInfoSheetData> updateSampleReportId(@Validated @RequestBody SampleReportIdUpdateVO sampleReportIdUpdateVO) {
        if (sampleReportIdUpdateVO.getReportIds().size() > 1) {
            return Result.defaultErrorByMessage("只能上传一个报告文件，一个项目支持一个文件报告");
        }
        sampleService.updateSampleReportId(sampleReportIdUpdateVO);
        return Result.defaultSuccessByMessage("操作成功");
    }


}
