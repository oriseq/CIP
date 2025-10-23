package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriseq.dtm.dto.PackageGroupDTO;
import com.oriseq.dtm.dto.PackageManageInfoDTO;
import com.oriseq.dtm.entity.Package;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Hacah
 * @className: PackageMapper
 * @date 2024/5/6 15:47
 */
public interface PackageMapper extends BaseMapper<Package> {
    /**
     * 所属套餐
     *
     * @return
     */
    List<PackageGroupDTO> selectBelongingPackage();

    List<PackageManageInfoDTO> selectPackageMangInfo(@Param("ew") QueryWrapper<Object> queryWp);

//    PackageVO getPackageInfo();

}
