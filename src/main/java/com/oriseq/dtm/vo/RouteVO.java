package com.oriseq.dtm.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由信息
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/16 13:46
 */
@Data
public class RouteVO {
    private String path;
    private String name;
    private String component;
    private JSONObject meta;
    private List<RouteVO> children = new ArrayList<>();

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Meta {
//        private String title;
//        /**
//         * 菜单图标
//         */
//        private String icon = "bx:bx-home";
//        private Integer orderNo;
//        /**
//         * 图片信息
//         */
//        private String img;
//
//        private boolean hideMenu = false;
//
//        public Meta(String permissionName, Integer orderNo, String imageUrl) {
//            this.title = permissionName;
//            this.orderNo = orderNo;
//            this.img = imageUrl;
//        }
//    }
}
