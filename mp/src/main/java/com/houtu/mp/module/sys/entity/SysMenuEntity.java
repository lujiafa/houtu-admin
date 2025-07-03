package com.houtu.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_menu
*
* @author houtu
* @since 2024-06-21
*/
@Getter
@Setter
@TableName("sys_menu")
public class SysMenuEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 菜单ID
    */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
    * 菜单名称
    */
    @TableField("menu_name")
    private String menuName;

    /**
    * 父菜单ID
    */
    @TableField("parent_id")
    private Long parentId;

    /**
    * 菜单类型 1-目录 2-菜单 3-功能
    */
    @TableField("menu_type")
    private Integer menuType;

    /**
    * 显示顺序
    */
    @TableField("sort")
    private Integer sort;

    /**
    * 地址类型 1-vue-router 2-外链
    */
    @TableField("path_type")
    private Integer pathType;

    /**
    * 菜单地址
    */
    @TableField("`path`")
    private String path;

    /**
    * 权限标识(menu_type为2/3且非外链时有效)
    */
    @TableField("perms")
    private String perms;

    /**
    * 菜单图标类型 1-ElementPlus原生图标 2-iconify图标
    */
    @TableField("icon_type")
    private Integer iconType;

    /**
    * 菜单图标
    */
    @TableField("icon")
    private String icon;

    /**
    * 状态 0-停用 1-启用/正常
    */
    @TableField("`status`")
    private Integer status;

    /**
    * 删除标志 0-未删除 1-已删除
    */
    @TableField("deleted")
    private Integer deleted;

    /**
    * 创建者
    */
    @TableField("create_by")
    private Long createBy;

    /**
    * 创建时间
    */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
    * 更新者
    */
    @TableField("update_by")
    private Long updateBy;

    /**
    * 更新时间
    */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
