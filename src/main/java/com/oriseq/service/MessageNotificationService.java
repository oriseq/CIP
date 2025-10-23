package com.oriseq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.dto.NeedAlarmSampleProjectDTO;
import com.oriseq.dtm.entity.MessageNotification;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author Hacah
 * @className: UsersService
 * @description: 用户
 * @date 2024/4/23 15:12
 */
public interface MessageNotificationService extends IService<MessageNotification> {


    /**
     * 发送通知
     */
    void sendNotifications(List<Long> userIds, MessageNotification messageNotification);


    /**
     * 在提交样本时发送通知
     * <p>
     * 此方法负责在样本提交过程中向相关方发送通知它通常会在样本处理系统中调用，
     * 用于通知相关人员或系统关于样本提交的最新情况
     *
     * @param username 提交样本的用户用户名
     * @param id       样本的唯一标识符
     * @param name     样本的名称
     */
    void sendNotificationsWhenSampleSubmission(String username, String id, String name);

    /**
     * 在申请新的合作项目时发送通知
     *
     * @param userGroupName 用户组名称
     */
    void sendNotificationsWhenApplyForANewCollaborativeProject(String userGroupName);


    /**
     * 样本过户时发送通知过户人
     *
     * @param userId           用户ID，用于标识发送通知的目标用户
     * @param username         用户名，触发操作的用户
     * @param sampleTransferId 样本ID
     */
    void sendNotificationsWhenSampleTransfer(Long userId, String username, @NotEmpty List<String> sampleTransferId);

    /**
     * 样本代管时发送通知代管人
     *
     * @param userId    用户ID，用于标识发送通知的目标用户
     * @param username  用户名，触发操作的用户
     * @param sampleIds 样本ID
     */
    void sendNotificationsWhenSampleCustody(Long userId, String username, @NotEmpty List<String> sampleIds);

    /**
     * 取消样本代管时发送通知接收代管的用户
     *
     * @param username  触发取消样本代管操作的用户用户名
     * @param sampleIds 被取消托管的样本ID列表
     */
    void sendNotificationsWhenCancelSampleCustody(String username, @NotEmpty List<String> sampleIds);

    /**
     * 样本报告全完成时发送通知
     */
    void sendNotificationsWhenSampleReportFullyCompleted(@NotEmpty List<Long> userIds, @NotNull Long sampleId);

    void sendSampleProjectAlarmMessage(NeedAlarmSampleProjectDTO needAlarmSampleProjectDTO);
}
