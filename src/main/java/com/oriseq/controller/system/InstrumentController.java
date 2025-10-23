package com.oriseq.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.entity.Instrument;
import com.oriseq.service.InstrumentService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 仪器控制器
 *
 * @author hacah
 * @version 1.0
 * @date 2025/7/3 9:27
 */
@RestController
@RequestMapping("system/instrument")
@Slf4j
@EnableLogging
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    /**
     * 查询所有仪器
     */
    @PostMapping("/list")
    public Result<List<Instrument>> list(@RequestBody Instrument instrument) {
        LambdaQueryWrapper<Instrument> queryWrapper = new LambdaQueryWrapper<Instrument>()
                .like(StringUtils.isNotBlank(instrument.getName()), Instrument::getName, instrument.getName())
                .like(StringUtils.isNotBlank(instrument.getRemarks()), Instrument::getRemarks, instrument.getRemarks());
        List<Instrument> list = instrumentService.list(queryWrapper);
        // 过滤导出字段
        if (instrument.getExportFields() != null && !instrument.getExportFields().isEmpty()) {
            list = list.stream()
                    .filter(item -> item.getExportFields() != null && item.getExportFields().containsAll(instrument.getExportFields()))
                    .collect(Collectors.toList());
        }
        return Result.defaultSuccessByMessageAndData("查询成功", list);
    }

    /**
     * 根据ID获取仪器信息
     */
    @GetMapping("/{id}")
    public Result<Instrument> getById(@PathVariable Long id) {
        Instrument instrument = instrumentService.getById(id);
        if (instrument == null) {
            return Result.defaultErrorByMessage("未找到该仪器");
        }
        return Result.defaultSuccessByMessageAndData("查询成功", instrument);
    }


    /**
     * 获取仪器名称和ID
     */
    @GetMapping("/simpleList")
    public Result<List<Instrument>> getSimpleList() {
        List<Instrument> instruments = instrumentService.list(
                new LambdaQueryWrapper<Instrument>()
                        .select(Instrument::getId, Instrument::getName)
        );
        return Result.defaultSuccessByMessageAndData("查询成功", instruments);
    }

    /**
     * 新增仪器
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody Instrument instrument) {
        boolean save = instrumentService.save(instrument);
        return Result.defaultSuccessByMessage("新增成功");
    }

    /**
     * 更新仪器
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody Instrument instrument) {
        boolean updateById = instrumentService.updateById(instrument);
        return Result.defaultSuccessByMessage("更新成功");
    }

    /**
     * 删除仪器
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean removeById = instrumentService.removeById(id);
        return Result.defaultSuccessByMessage("删除成功");
    }
}
