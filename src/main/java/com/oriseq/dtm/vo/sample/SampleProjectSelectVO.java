package com.oriseq.dtm.vo.sample;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SampleProjectSelectVO {

    @NotEmpty
    private List<Long> sampleIds;

    /**
     * 使用场景
     * 不填默认所有查询
     * uploadReports：只查询非（取消状态和无报告）的项目
     * updateProjectStatus：查询所有的项目
     * updateProjectStatusByInstrument：查询所有的项目
     */
    private String scene;


}
