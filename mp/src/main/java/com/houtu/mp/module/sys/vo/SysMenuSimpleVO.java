package com.houtu.mp.module.sys.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysMenuSimpleVO extends SysMenuQueryBaseVO {

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
     * 权限标识集合，表示当前菜单拥有哪些权限(menu_type为2且非外链时有效)
     */
    private List<String> perms;

    /**
     * 菜单图标类型 1-ElementPlus原生图标 2-iconify图标
     */
    private Integer iconType;

    /**
     * 菜单图标
     */
    private String icon;

}