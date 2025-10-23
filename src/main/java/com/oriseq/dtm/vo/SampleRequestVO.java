package com.oriseq.dtm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SampleRequestVO {
    private String name;
    private String sex;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate birthday;
    private List<Long> projects;
    private String status;
    private String logisticsTrackingNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> submissionTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> updateTime;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 样本编号
     */
    private String sampleId;

    /**
     * 样本归属用户组ID
     */
    private Long sampleUserGroupId;


    /**
     * 排序列
     */
    private String field;

    /**
     * 排序方式
     * ascend
     * descend
     */
    private String order;
    private Integer page = 1;
    private Integer pageSize = 10;

    /**
     * 导出条件
     * 1. 全部
     * 2. 分类 - 全部
     * 3. 分类 - 仅外送项目
     * 4. 分类 - 仅自检项目
     * 5. 分类-指定仪器
     */
    private Integer exportCondition = 1;


    /**
     * 导出条件，导出指定的样本中的数据
     */
    private List<Long> sampleIds;


    /**
     * scene 场景
     * 送检任务管理：inspection 仅返回非全部完成的样本
     * 归档任务管理：archive 返回全部完成的样本
     */
    private String inspectionScene;

    /**
     * 仪器ID列表：包含所选仪器对应项目的样本
     */
    private List<Long> instrumentIds;

    /**
     * 外送单位ID列表：包含所选外送单位的项目的样本
     */
//    private List<Long> deliveryUnitIds;
    /**
     * 外送单位名称列表
     */
    private List<String> deliveryUnits;


    /**
     * 项目状态列表：包含所选项目状态的项目的样本
     */
    private List<Integer> projectStatuses;


}
