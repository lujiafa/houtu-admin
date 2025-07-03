package com.houtu.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysMenuQueryVO extends SysMenuQueryBaseVO {

    /**
     * 菜单类型 1-目录 2-菜单 3-功能
     */
    private Integer menuType;

    /**
     * 地址类型 1-vue-router 2-外链
     */
    private Integer pathType;

    /**
     * 菜单地址
     */
    private String path;

    /**
     * 权限标识(menu_type为2/3且非外链时有效)
     */
    private String perms;

    /**
     * 菜单图标类型 1-ElementPlus原生图标 2-iconify图标
     */
    private Integer iconType;

    /**
     * 菜单图标
     */
    private String icon;

    private Integer sort;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}