package com.oriseq.controller.system;

import cn.hutool.json.JSONObject;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.vo.billStatistics.BillStatisticsVO;
import com.oriseq.service.ProjectReferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: 账单统计
 *
 * @author hacah
 * @version 1.0
 * @date 2025/4/8 16:15
 */
@RestController
@RequestMapping("/billStatistics")
@EnableLogging
public class BillStatisticsController {

    @Autowired
    private ProjectReferencesService projectReferencesService;
    @Autowired
    private LoginTool loginTool;

    @PostMapping("/list")
    public Result<List<JSONObject>> list(@RequestBody(required = false) BillStatisticsVO billStatisticsVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        List<JSONObject> jsonObjects = projectReferencesService.staticProjectNumAndSpend(billStatisticsVO, userInfo);
        return Result.defaultSuccessByMessageAndData("查询成功", jsonObjects);
    }
}
