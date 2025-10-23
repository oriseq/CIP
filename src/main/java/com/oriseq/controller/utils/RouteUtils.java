package com.oriseq.controller.utils;

import cn.hutool.core.util.StrUtil;
import com.oriseq.dtm.entity.Permission;
import com.oriseq.dtm.vo.RouteVO;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * 路由工具
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/16 14:17
 */
public class RouteUtils {

    @NotNull
    public static RouteVO getRouteVO(Permission permission) {
        RouteVO routeVO = new RouteVO();
        String[] split = Optional.ofNullable(permission.getRoute()).get().split("/");
        if (split.length > 0) {
            String path = split[split.length - 1];
            if (StrUtil.isNotBlank(permission.getMenuNamePath())) {
                routeVO.setName(capitalizeFirstLetter(permission.getMenuNamePath()));
                routeVO.setPath(permission.getMenuNamePath());
            } else {
                routeVO.setName(capitalizeFirstLetter(path));
                routeVO.setPath(path);
            }
        }
        routeVO.setComponent(permission.getRoute() + "/index");
        routeVO.setMeta(permission.getMeta());
        return routeVO;
    }

    public static String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        char firstChar = Character.toUpperCase(word.charAt(0));
        return firstChar + word.substring(1);
    }

}
