package com.oriseq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.entity.MessageType;
import com.oriseq.mapper.MessageTypeMapper;
import com.oriseq.service.MessageTypeService;
import org.springframework.stereotype.Service;

/**
 * @author huang
 * @description 针对表【message_type】的数据库操作Service实现
 * @createDate 2025-09-19 14:22:54
 */
@Service
public class MessageTypeServiceImpl extends ServiceImpl<MessageTypeMapper, MessageType>
        implements MessageTypeService {

}




