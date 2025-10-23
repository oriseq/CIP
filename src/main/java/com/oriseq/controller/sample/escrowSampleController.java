package com.oriseq.controller.sample;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.vo.SampleRequestVO;
import com.oriseq.dtm.vo.sample.SampleInfoVO;
import com.oriseq.service.IProjectService;
import com.oriseq.service.ISampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class escrowSampleController {


    @Autowired
    private LoginTool loginTool;

    @Autowired
    private ISampleService sampleService;

    @Autowired
    private IProjectService projectService;

    /**
     * 获取送检样本信息
     *
     * @return
     */
    @PostMapping("escrowSampleInformation")
    public Result<IPage<SampleInfoVO>> sampleInformation(
            @RequestBody(required = false) SampleRequestVO sampleRequestVO) {
        LoginUser user = loginTool.getUserInfo();
        IPage<SampleInfoVO> sampleInfoVOIPage = sampleService.getEscrowSampleInfo(user, sampleRequestVO);
        return Result.defaultSuccessByMessageAndData("获取成功", sampleInfoVOIPage);
    }


}
