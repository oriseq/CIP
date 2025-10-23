package com.oriseq.controller.system;

import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectItemizedSpendDTO;
import com.oriseq.dtm.vo.ItemizedBillStatisticsVO;
import com.oriseq.service.ItemizedBillStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: 账单明细统计
 *
 * @author hacah
 * @version 1.0
 * @date 2025/4/8 16:15
 */
@RestController
@RequestMapping("/itemizedBill")
@EnableLogging
public class ItemizedBillStatisticsController {

    @Autowired
    private ItemizedBillStatisticsService itemizedBillStatisticsService;
    @Autowired
    private LoginTool loginTool;

    @PostMapping("/list")
    public Result<List<ProjectItemizedSpendDTO>> list(@RequestBody(required = false) ItemizedBillStatisticsVO vo) {
        LoginUser userInfo = loginTool.getUserInfo();
        List<ProjectItemizedSpendDTO> projectItemizedSpendDTOS = itemizedBillStatisticsService.staticProjectItemizedSpend(vo, userInfo);
        return Result.defaultSuccessByMessageAndData("查询成功", projectItemizedSpendDTOS);
    }
}
