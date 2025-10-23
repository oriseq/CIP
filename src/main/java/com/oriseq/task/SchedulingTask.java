package com.oriseq.task;

import com.oriseq.dtm.dto.NeedAlarmSampleProjectDTO;
import com.oriseq.dtm.entity.SampleProject;
import com.oriseq.service.ISampleProjectService;
import com.oriseq.service.MessageNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务
 *
 * @author hacah
 * @version 1.0
 * @date 2024/9/6 13:24
 */
@Slf4j
@Component
public class SchedulingTask {

    @Autowired
    private ISampleProjectService sampleProjectService;

    @Autowired
    private MessageNotificationService messageNotificationService;


    /**
     * 样本项目告警任务
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 30)
    public void loadSampleProjectAlarmTask() {
        log.info("开始，样本项目告警任务");
        List<NeedAlarmSampleProjectDTO> needAlarmSampleProject = sampleProjectService.getNeedAlarmSampleProject();
        for (NeedAlarmSampleProjectDTO needAlarmSampleProjectDTO : needAlarmSampleProject) {
            // 计算告警时间
            LocalDateTime deadline = needAlarmSampleProjectDTO.getDeadline();
            String remainingHour = needAlarmSampleProjectDTO.getRemainingHour();
            // 减去remainingHour小时, 得到告警时间点
            LocalDateTime alarmTime = deadline.minusHours(Long.parseLong(remainingHour));
            // 当前时间是否超过告警时间点
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(alarmTime)) {
                log.info("开始，发送告警消息");
                // 发送告警消息
                messageNotificationService.sendSampleProjectAlarmMessage(needAlarmSampleProjectDTO);
                // 设置状态已发送
                SampleProject sampleProject = new SampleProject();
                sampleProject.setId(needAlarmSampleProjectDTO.getId());
                sampleProject.setIsAlarmSent(true);
                sampleProjectService.updateById(sampleProject);
            }
        }
        log.info("结束，样本项目告警任务");
    }


}
