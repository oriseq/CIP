package com.oriseq.controller.basis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.MessageNotification;
import com.oriseq.dtm.enums.MessageType;
import com.oriseq.dtm.vo.message.*;
import com.oriseq.service.MessageNotificationService;
import com.oriseq.service.MessageTypeService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * 消息
 *
 * @author huang
 */
@RestController
@RequestMapping("message")
@Slf4j
public class MessageNotificationController {

    @Autowired
    private MessageNotificationService messageNotificationService;
    @Autowired
    private LoginTool loginTool;
    @Autowired
    private MessageTypeService messageTypeService;

    /**
     * 获取消息
     *
     * @param userId
     * @return
     */
    @GetMapping("/msg/{id}")
    public Result<List<MessageNotification>> getMessages(@PathVariable("id") Integer userId) {
        LambdaQueryWrapper<MessageNotification> qw = new LambdaQueryWrapper<MessageNotification>()
                .eq(MessageNotification::getUserId, userId).and(messageNotificationLambdaQueryWrapper -> {
                    messageNotificationLambdaQueryWrapper.eq(MessageNotification::getStatus, 0)
                            .or()
                            .eq(MessageNotification::getStatus, 1);
                })
                .orderBy(true, false, MessageNotification::getDatetime);
        List<MessageNotification> list = messageNotificationService.list(qw);
        return Result.defaultSuccessByMessageAndData("获取消息成功", list);
    }

    /**
     * 是否有新消息
     *
     * @param userId
     * @return
     */
    @GetMapping("/countNewMsg/{id}")
    public Result<Long> isNewMsg(@PathVariable("id") Integer userId) {
        LambdaQueryWrapper<MessageNotification> qw = new LambdaQueryWrapper<MessageNotification>()
                .eq(MessageNotification::getUserId, userId).and(messageNotificationLambdaQueryWrapper -> {
                    messageNotificationLambdaQueryWrapper.eq(MessageNotification::getStatus, 0);
                })
                .orderBy(true, false, MessageNotification::getDatetime);
        long count = messageNotificationService.count(qw);
        return Result.defaultSuccessByMessageAndData("获取消息成功", count);
    }


    /**
     * 发送信息（保存）
     *
     * @return
     */
    @PostMapping("/msg")
    public Result<List<MessageNotification>> saveMessages(@Validated @RequestBody MessageNotificationVO messageNotificationVO) {
        // 类型是 待办
        if (messageNotificationVO.getType() == MessageType.TODO.getValue()) {
            if (StringUtils.isBlank(messageNotificationVO.getExtra())) {
                throw new RuntimeException("待办类型必须填写额外信息");
            }
        }
        List<Long> userId = messageNotificationVO.getUserIds();
        List<MessageNotification> messageNotifications = userId.stream().map(id -> {
            MessageNotification messageNotification = new MessageNotification();
            BeanUtils.copyProperties(messageNotificationVO, messageNotification);
            messageNotification.setStatus(0);
            messageNotification.setUserId(id);
            messageNotification.setDatetime(LocalDateTime.now());
            return messageNotification;
        }).toList();
        messageNotificationService.saveBatch(messageNotifications);
        return Result.defaultSuccessByMessage("消息发送成功");
    }

    /**
     * 更新状态-删除信息
     *
     * @return
     */
    @PutMapping("/msgDel")
    public Result<List<MessageNotification>> msgDel(@Validated @RequestBody UpdateMessageStatusVO updateMessageStatusVO) {
        if (updateMessageStatusVO.getCheckAll() != null) {
            if (updateMessageStatusVO.getCheckAll()) {
                LoginUser userInfo = loginTool.getUserInfo();
                LambdaUpdateWrapper<MessageNotification> updateWrapper = new LambdaUpdateWrapper<MessageNotification>()
                        .eq(MessageNotification::getUserId, userInfo.getUserId())
                        .eq(MessageNotification::getType, MessageType.NOTIFICATION.getValue())
                        .set(MessageNotification::getStatus, 2);
                messageNotificationService.update(updateWrapper);
            }
        } else {
            if (updateMessageStatusVO.getIds() == null || updateMessageStatusVO.getIds().size() == 0) {
                throw new RuntimeException("请选择设置的消息");
            }
            List<Long> ids = updateMessageStatusVO.getIds();
            List<MessageNotification> messageNotifications = ids.stream().map(id -> {
                MessageNotification messageNotification = new MessageNotification();
                messageNotification.setStatus(2);
                messageNotification.setId(id);
                return messageNotification;
            }).toList();
            messageNotificationService.updateBatchById(messageNotifications);
        }
        return Result.defaultSuccessByMessage("设置已读状态成功");
    }

    /**
     * 设置已读
     *
     * @return
     */
    @PutMapping("/msgRead")
    public Result<List<MessageNotification>> updateMsgRead(@Validated @RequestBody UpdateMsgReadVO updateMsgReadVO) {
        LambdaUpdateWrapper<MessageNotification> updateWrapper = new LambdaUpdateWrapper<MessageNotification>()
                .eq(MessageNotification::getUserId, updateMsgReadVO.getUserId())
                .eq(MessageNotification::getType, updateMsgReadVO.getType())
                .eq(MessageNotification::getStatus, 0)
                .set(MessageNotification::getStatus, 1);
        messageNotificationService.update(updateWrapper);
        return Result.defaultSuccessByMessage("设置成功");
    }

    /**
     * 更新消息，提供待办的更新
     *
     * @return
     */
    @PutMapping("/msg")
    public Result<List<MessageNotification>> updateMsgRead(@Validated @RequestBody UpdateMessageNotificationVO updateMessageNotificationVO) {
        // 是否是待办
        if (updateMessageNotificationVO.getType() != MessageType.TODO.getValue()) {
            throw new RuntimeException("只有待办类型可以更新");
        }
        MessageNotification byId = messageNotificationService.getById(updateMessageNotificationVO.getId());
        Integer type = byId.getType();
        if (type != MessageType.TODO.getValue()) {
            throw new RuntimeException("只有待办类型可以更新");
        }

        MessageNotification messageNotification = new MessageNotification();
        BeanUtils.copyProperties(updateMessageNotificationVO, messageNotification);
        messageNotificationService.updateById(messageNotification);
        return Result.defaultSuccessByMessage("设置成功");
    }


    /**
     * 消息管理-获取all消息
     *
     * @param messageAllVO 参数
     * @return
     */
    @PostMapping("/msgAll")
    public Result<List<MessageNotification>> getAllMessages(@RequestBody MessageAllVO messageAllVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        LambdaQueryWrapper<MessageNotification> qw = new LambdaQueryWrapper<MessageNotification>()
                .eq(MessageNotification::getUserId, userInfo.getUserId())
                .and(messageAllVO.getType() != null, aqw -> {
                    aqw.eq(messageAllVO.getType() != null, MessageNotification::getType, messageAllVO.getType())
                            .eq(StringUtils.isNotBlank(messageAllVO.getSecondaryType()), MessageNotification::getSecondaryType, messageAllVO.getSecondaryType())
                            //查收类型
                            //	未查收
                            //		对应status等于0或1
                            //	已查收
                            //		对应status等于2
                            .in(messageAllVO.getExternalState() != null && messageAllVO.getExternalState() == 0, MessageNotification::getStatus, 0, 1)
                            .eq(messageAllVO.getExternalState() != null && messageAllVO.getExternalState() == 1, MessageNotification::getStatus, 2)
                            .between(messageAllVO.getDatetime() != null && !messageAllVO.getDatetime().isEmpty(),
                                    MessageNotification::getDatetime,
                                    Optional.ofNullable(messageAllVO.getDatetime()).map(list -> list.get(0)).orElse(null)
                                    , Optional.ofNullable(messageAllVO.getDatetime()).map(list -> list.get(1)).orElse(null))
                            .nested(StringUtils.isNotBlank(messageAllVO.getContent()), qw2 -> qw2.like(StringUtils.isNotBlank(messageAllVO.getContent()), MessageNotification::getTitle, messageAllVO.getContent()).or()
                                    .like(StringUtils.isNotBlank(messageAllVO.getContent()), MessageNotification::getDescription, messageAllVO.getContent()));
                })
                .orderBy(true, false, MessageNotification::getDatetime);
        List<MessageNotification> list = messageNotificationService.list(qw);
        return Result.defaultSuccessByMessageAndData("获取消息成功", list);
    }


    /**
     * 消息管理-所有二级分类
     *
     * @param map 参数
     * @return
     */
    @PostMapping("/msgSecondaryType")
    public Result<List<MessageSecondaryTypeVO>> getMsgSecondaryType(@RequestBody HashMap<String, String> map) {
        String type = map.get("type");
        LambdaQueryWrapper<MessageNotification> qw = new LambdaQueryWrapper<MessageNotification>()
                .eq(MessageNotification::getType, type);
        List<String> list = SimpleQuery.list(qw,
                MessageNotification::getSecondaryType,
                messageNotification -> System.out.println());
        List<MessageSecondaryTypeVO> messageSecondaryTypeVOS = list.stream()
                .distinct()
                .filter(string -> StringUtils.isNotBlank(string))
                .map(string -> {
                    MessageSecondaryTypeVO messageSecondaryTypeVO = new MessageSecondaryTypeVO();
                    messageSecondaryTypeVO.setSecondaryType(string);
                    return messageSecondaryTypeVO;
                }).toList();
        return Result.defaultSuccessByMessageAndData("获取分类成功", messageSecondaryTypeVOS);
    }


    /**
     * 获取通知类别-所有二级分类
     *
     * @return
     */
    @GetMapping("/notificationSecondaryTypes")
    public Result<List<com.oriseq.dtm.entity.MessageType>> getNotificationSecondaryTypes() {
        List<com.oriseq.dtm.entity.MessageType> list = messageTypeService.list();
        return Result.defaultSuccessByMessageAndData("获取分类成功", list);
    }

}
