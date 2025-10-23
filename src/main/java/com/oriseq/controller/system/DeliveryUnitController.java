package com.oriseq.controller.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.permission.RequiredPermission;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.entity.DeliveryUnit;
import com.oriseq.dtm.vo.deliveryUnit.DeliveryUnitDeleteVO;
import com.oriseq.dtm.vo.deliveryUnit.DeliveryUnitQueryVO;
import com.oriseq.service.DeliveryUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 外送单位管理Controller
 *
 * @author hacah
 * @version 1.0
 * @date [Current Date]
 */
@RestController
@RequestMapping("/system/deliveryUnit")
@EnableLogging
public class DeliveryUnitController {

    @Autowired
    private DeliveryUnitService deliveryUnitService;

    /**
     * 查询外送单位列表
     *
     * @param queryVO
     * @return
     */
    @PostMapping("/list")
    @RequiredPermission("system:deliveryUnit:query")
    public Result<List<DeliveryUnit>> list(@RequestBody(required = false) DeliveryUnitQueryVO queryVO) {
        LambdaQueryWrapper<DeliveryUnit> wrapper = Wrappers.lambdaQuery();
        if (queryVO != null) {
            wrapper.like(StrUtil.isNotBlank(queryVO.getName()), DeliveryUnit::getName, queryVO.getName())
                    .like(StrUtil.isNotBlank(queryVO.getRemarks()), DeliveryUnit::getRemarks, queryVO.getRemarks());
        }
        List<DeliveryUnit> list = deliveryUnitService.list(wrapper);
        return Result.defaultSuccessByMessageAndData("查询成功", list);
    }

    /**
     * 新增外送单位
     *
     * @param deliveryUnit
     * @return
     */
    @PostMapping("")
    @RequiredPermission("system:deliveryUnit:add")
    public Result<Void> add(@RequestBody DeliveryUnit deliveryUnit) {
        deliveryUnit.setSubmissionTime(LocalDateTime.now());
        deliveryUnit.setUpdateTime(LocalDateTime.now());
        deliveryUnitService.save(deliveryUnit);
        return Result.defaultSuccessByMessage("新增成功");
    }

    /**
     * 更新外送单位
     *
     * @param deliveryUnit
     * @return
     */
    @RequiredPermission("system:deliveryUnit:update")
    @PutMapping("")
    public Result<Void> update(@RequestBody DeliveryUnit deliveryUnit) {
        deliveryUnit.setUpdateTime(LocalDateTime.now());
        deliveryUnitService.updateById(deliveryUnit);
        return Result.defaultSuccessByMessage("更新成功");
    }

    /**
     * 删除外送单位
     *
     * @param deliveryUnitDeleteVO
     * @return
     */
    @DeleteMapping("")
    @RequiredPermission("system:deliveryUnit:delete")
    public Result<Void> delete(@Validated @RequestBody DeliveryUnitDeleteVO deliveryUnitDeleteVO) {
        deliveryUnitService.removeBatchByIds(deliveryUnitDeleteVO.getDeliveryUnitIds());
        return Result.defaultSuccessByMessage("删除成功");
    }

}