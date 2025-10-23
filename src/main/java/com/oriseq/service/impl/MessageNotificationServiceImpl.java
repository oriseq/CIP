package com.oriseq.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.dto.NeedAlarmSampleProjectDTO;
import com.oriseq.dtm.entity.*;
import com.oriseq.dtm.enums.MessageType;
import com.oriseq.mapper.MessageNotificationMapper;
import com.oriseq.mapper.SampleHostingMapper;
import com.oriseq.mapper.SampleMapper;
import com.oriseq.service.MessageNotificationService;
import com.oriseq.service.MessageTypeService;
import com.oriseq.service.MsgGroupService;
import com.oriseq.service.SysConfigService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/9 13:51
 */
@Service
@Slf4j
public class MessageNotificationServiceImpl extends ServiceImpl<MessageNotificationMapper, MessageNotification> implements MessageNotificationService {

    @Autowired
    private MsgGroupService msgGroupService;
    @Autowired
    private SampleMapper sampleMapper;
    @Autowired
    private SampleHostingMapper sampleHostingMapper;
    @Autowired
    private MessageTypeService messageTypeService;
    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public void sendNotifications(List<Long> userIds, MessageNotification messageNotification) {
        List<MessageNotification> messageNotifications = userIds.stream().map(id -> {
            MessageNotification m = new MessageNotification();
            BeanUtils.copyProperties(messageNotification, m);
            m.setStatus(0);
            m.setUserId(id);
            m.setType(MessageType.NOTIFICATION.getValue());
            m.setDatetime(LocalDateTime.now());
            return m;
        }).toList();
        boolean b = this.saveBatch(messageNotifications);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendNotificationsWhenSampleSubmission(String username, String id, String name) {
        Long secondaryTypeId = 1L;
        // 检查该类型的通知是否启用
        if (!canSendNotification(secondaryTypeId)) {
            return;
        }
        // 1.取需要发送的用户id
        LambdaQueryWrapper<MsgGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgGroup::getGroupIdentify, "inspection_form_submission");
        MsgGroup group = msgGroupService.getOne(wrapper);
        JSONArray userIds1 = group.getUserIds();
        List<Long> userIds2 = userIds1.toList(Long.class);
        // 构建消息对象
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setTitle("样本提交");
        // 消息模板：用户【{用户名}】提交了一个新样本：【{样本编号}】【{样本姓名}】，请及时处理。
        messageNotification.setDescription(
                MessageFormat.format("用户【{0}】提交了一个新样本：编号【{1}】 姓名【{2}】，请及时处理。"
                        , username, id, name));
        com.oriseq.dtm.entity.MessageType byId = messageTypeService.getById(secondaryTypeId);
        messageNotification.setSecondaryType(byId.getMsgType());
        this.sendNotifications(userIds2, messageNotification);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendNotificationsWhenApplyForANewCollaborativeProject(String userGroupName) {
        Long secondaryTypeId = 2L;
        // 检查该类型的通知是否启用
        if (!canSendNotification(secondaryTypeId)) {
            return;
        }
        // 1.取需要发送的用户id
        LambdaQueryWrapper<MsgGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgGroup::getGroupIdentify, "project_management");
        MsgGroup group = msgGroupService.getOne(wrapper);
        JSONArray userIds1 = group.getUserIds();
        List<Long> userIds2 = userIds1.toList(Long.class);
        // 构建消息对象
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setTitle("新合作项目申请");
        // 用户组【{用户组名}】申请新合作项目，请到项目审批中查看详情。
        messageNotification.setDescription(MessageFormat.format("用户组【{0}】申请新合作项目，请到项目审批中查看详情。", userGroupName));
        com.oriseq.dtm.entity.MessageType byId = messageTypeService.getById(secondaryTypeId);
        messageNotification.setSecondaryType(byId.getMsgType());
        this.sendNotifications(userIds2, messageNotification);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendNotificationsWhenSampleTransfer(Long userId, String username, @NotEmpty List<String> sampleTransferId) {
        Long secondaryTypeId = 3L;
        // 检查该类型的通知是否启用
        if (!canSendNotification(secondaryTypeId)) {
            return;
        }
        // 1.取需要发送的用户id
        List<Long> userIds = List.of(userId);
        // 样本信息
        StringBuilder text = new StringBuilder();
        List<String> formattedSamples = sampleTransferId.stream()
                .map(id -> {
                    Sample sample = sampleMapper.selectById(id);
                    return Optional.ofNullable(sample)
                            .map(s -> MessageFormat.format("编号【{0}】 姓名【{1}】", s.getId(), s.getName()))
                            // 如果 sample 为空，返回空字符串
                            .orElse("");
                })
                .collect(Collectors.toList());
        text.append(String.join("、", formattedSamples));
        // 去掉最后一个顿号（如果存在）
        if (text.length() > 0 && text.charAt(text.length() - 1) == '、') {
            text.deleteCharAt(text.length() - 1);
        }
        // 构建消息对象
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setTitle("样本过户");
        // 接收到用户【{用户名}】的过户样本：编号【{样本编号}】，姓名【{样本姓名}】、...
        messageNotification.setDescription(MessageFormat.format("接收到用户【{0}】的过户样本：{1}", username, text.toString()));
        com.oriseq.dtm.entity.MessageType byId = messageTypeService.getById(secondaryTypeId);
        messageNotification.setSecondaryType(byId.getMsgType());
        this.sendNotifications(userIds, messageNotification);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendNotificationsWhenSampleCustody(Long userId, String username, @NotEmpty List<String> sampleIds) {
        Long secondaryTypeId = 4L;
        // 检查该类型的通知是否启用
        if (!canSendNotification(secondaryTypeId)) {
            return;
        }
        // 1.取需要发送的用户id
        List<Long> userIds = List.of(userId);
        // 样本信息
        StringBuilder text = new StringBuilder();
        List<String> formattedSamples = sampleIds.stream()
                .map(id -> {
                    Sample sample = sampleMapper.selectById(id);
                    return Optional.ofNullable(sample)
                            .map(s -> MessageFormat.format("编号【{0}】 姓名【{1}】", s.getId(), s.getName()))
                            // 如果 sample 为空，返回空字符串
                            .orElse("");
                })
                .collect(Collectors.toList());
        text.append(String.join("、", formattedSamples));
        // 去掉最后一个顿号（如果存在）
        if (text.length() > 0 && text.charAt(text.length() - 1) == '、') {
            text.deleteCharAt(text.length() - 1);
        }
        // 构建消息对象
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setTitle("样本代管");
        messageNotification.setDescription(MessageFormat.format("接收到用户【{0}】的代管样本：{1}", username, text.toString()));
        com.oriseq.dtm.entity.MessageType byId = messageTypeService.getById(secondaryTypeId);
        messageNotification.setSecondaryType(byId.getMsgType());
        this.sendNotifications(userIds, messageNotification);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendNotificationsWhenCancelSampleCustody(String username, @NotEmpty List<String> sampleIds) {
        Long secondaryTypeId = 5L;
        // 检查该类型的通知是否启用
        if (!canSendNotification(secondaryTypeId)) {
            return;
        }
        for (String sampleId : sampleIds) {
            // 1.取需要发送的用户id
            List<SampleHosting> sampleHostings = sampleHostingMapper.selectList(new LambdaQueryWrapper<SampleHosting>().eq(SampleHosting::getSampleId, sampleId));
            List<Long> sendUserList = sampleHostings.stream().map(sampleHosting -> {
                Long hostingUserId = sampleHosting.getHostingUserId();
                return hostingUserId;
            }).toList();
            // 2.样本信息
            Sample sample = sampleMapper.selectById(sampleId);
            // 消息模板：用户【{用户名}】已取消代管样本：编号【{样本编号}】，姓名【{样本姓名}】
            String description = MessageFormat.format("用户【{0}】已取消代管样本：编号【{1}】 姓名【{2}】", username, sample.getId(), sample.getName());
            // 构建消息对象
            MessageNotification messageNotification = new MessageNotification();
            messageNotification.setTitle("取消样本代管");
            messageNotification.setDescription(description);
            com.oriseq.dtm.entity.MessageType byId = messageTypeService.getById(secondaryTypeId);
            messageNotification.setSecondaryType(byId.getMsgType());
            this.sendNotifications(sendUserList, messageNotification);


        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendNotificationsWhenSampleReportFullyCompleted(@NotEmpty List<Long> userIds, @NotNull Long sampleId) {
        Long secondaryTypeId = 6L;
        // 检查该类型的通知是否启用
        if (!canSendNotification(secondaryTypeId)) {
            return;
        }
        // 1.取需要发送的用户id
        // 发送信息
        Sample sample = sampleMapper.selectById(sampleId);
        // 构建消息对象
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setTitle("样本报告全部完成");
        // 样本：编号【{样本编号}】，姓名【{样本姓名}】报告已全部完成
        messageNotification.setDescription(MessageFormat.format("样本：编号【{0}】 姓名【{1}】报告已全部完成", sample.getId(), sample.getName()));
        com.oriseq.dtm.entity.MessageType byId = messageTypeService.getById(secondaryTypeId);
        messageNotification.setSecondaryType(byId.getMsgType());
        this.sendNotifications(userIds, messageNotification);

    }

    @Override
    public void sendSampleProjectAlarmMessage(NeedAlarmSampleProjectDTO needAlarmSampleProjectDTO) {
        Long secondaryTypeId = 7L;
        // 检查该类型的通知是否启用
        if (!canSendNotification(secondaryTypeId)) {
            return;
        }
        // 得到提交者id
        Long sampleSubmitterUserId = needAlarmSampleProjectDTO.getSampleSubmitterUserId();
        // 得到代管用户
        List<SampleHosting> sampleHostings = sampleHostingMapper.selectList(new LambdaQueryWrapper<SampleHosting>().eq(SampleHosting::getSampleId, needAlarmSampleProjectDTO.getSampleId()));
        List<Long> subSendUserList = sampleHostings.stream().map(sampleHosting -> {
            Long hostingUserId = sampleHosting.getHostingUserId();
            return hostingUserId;
        }).toList();
        List<Long> sendUserList = new ArrayList<>(subSendUserList);
        sendUserList.add(sampleSubmitterUserId);

        // 消息模板：样本：编号【{样本编号}】 姓名【{样本姓名}】 项目名【{名称}】已临近截止时间【{截至时间}】,请及时处理
        String description = MessageFormat.format("样本编号【{0}】 姓名【{1}】 项目名【{2}】已临近截止时间【{3}】,请及时处理",
                needAlarmSampleProjectDTO.getSampleId(), needAlarmSampleProjectDTO.getName(),
                needAlarmSampleProjectDTO.getProjectName(),
                needAlarmSampleProjectDTO.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // 构建消息对象
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setTitle("样本项目截止时间提醒");
        messageNotification.setDescription(description);
        com.oriseq.dtm.entity.MessageType byId = messageTypeService.getById(secondaryTypeId);
        messageNotification.setSecondaryType(byId.getMsgType());
        this.sendNotifications(sendUserList, messageNotification);


    }

    /**
     * 判断指定的消息二级类型是否启用（是否在系统配置的启用列表中）
     *
     * @param msgSecondaryTypeId 消息二级类型ID
     * @return 如果启用返回true，否则返回false
     */
    private boolean canSendNotification(Long msgSecondaryTypeId) {
        SysConfig one = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, "notify.basic.use"));
        String configValue = one.getConfigValue();
        // 转成json
        if (StrUtil.isNotBlank(configValue)) {
            JSONArray objects = JSONUtil.parseArray(configValue);
            List<String> list = objects.toList(String.class);
            return list.contains(msgSecondaryTypeId.toString());
        }
        return false;
    }


}
