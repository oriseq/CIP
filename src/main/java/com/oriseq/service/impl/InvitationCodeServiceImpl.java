package com.oriseq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.entity.InvitationCode;
import com.oriseq.mapper.InvitationCodeMapper;
import com.oriseq.service.IInvitationCodeService;
import org.springframework.stereotype.Service;

/**
 * @author Hacah
 * @className: InvitationCodeServiceImpl
 * @date 2024/5/6 15:37
 */
@Service
public class InvitationCodeServiceImpl extends ServiceImpl<InvitationCodeMapper, InvitationCode> implements IInvitationCodeService {
}
