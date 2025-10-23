package com.oriseq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.oriseq.controller.utils.JwtUtils;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.entity.UserGroup;
import com.oriseq.dtm.vo.LoginUserVO;
import com.oriseq.mapper.UserGroupMapper;
import com.oriseq.mapper.UsersMapper;
import com.oriseq.service.ISmallProceduresService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * 小程序service
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/28 14:31
 */
@Service
public class SmallProceduresServiceImpl implements ISmallProceduresService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginUserVO setUserInfo(Map rsp) {
        String errcode = (String) rsp.get("errcode");
        if (StringUtils.isNotBlank(errcode) && errcode.equals("0")) {
            Map phoneInfo = new Gson().fromJson((String) rsp.get("phone_info"), Map.class);
            // 获取手机号
            String phoneNumber = (String) phoneInfo.get("purePhoneNumber");
            // 查询数据库 是否存在用户
            User one = usersMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, phoneNumber));
            if (one == null) {
                throw new RuntimeException("用户不存在，请先注册");
            }
            // 查询单位
            UserGroup userGroup = userGroupMapper.selectById(one.getUserGroupId());

            usersMapper.updateById(one);
            // 生成token
            String token = JwtUtils.generateToken(one.getPhoneNumber());
            LoginUserVO loginUserVO = new LoginUserVO();
            loginUserVO.setToken(token);
            loginUserVO.setUserId(one.getId().toString());
            loginUserVO.setUsername(one.getUsername());
            loginUserVO.setUserGroupName(Optional.ofNullable(userGroup).get().getGroupName());
            return loginUserVO;
        }
        return null;
    }
}
