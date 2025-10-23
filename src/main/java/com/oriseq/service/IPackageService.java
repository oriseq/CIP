package com.oriseq.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.PackageManageInfoDTO;
import com.oriseq.dtm.entity.Package;
import com.oriseq.dtm.vo.PackageVO;
import com.oriseq.dtm.vo.packageInfo.PackageSearchVO;

import java.util.Collection;
import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:36
 */
public interface IPackageService extends IService<Package> {

    /**
     * 获取套餐信息
     *
     * @param userInfo
     * @return
     */
    List<PackageVO> getPackageInfo(LoginUser userInfo);

    /**
     * 获取用户组的套餐数据
     *
     * @param userInfo
     * @return
     */
    Collection<JSONObject> getBelongingPackageCascader(LoginUser userInfo);

    /**
     * 获取套餐信息
     *
     * @return
     */
    List<PackageManageInfoDTO> getPackageManageInfo(LoginUser userInfo, PackageSearchVO packageSearchVO);
}
