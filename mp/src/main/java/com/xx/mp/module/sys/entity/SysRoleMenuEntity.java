package com.xx.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
* sys_role_menu
*
* @author houtu
* @since 2024-06-24
*/
@Getter
@Setter
@TableName("sys_role_menu")
public class SysRoleMenuEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 角色ID
    */
    @TableField("role_id")
    private Long roleId;

    /**
    * 菜单ID
    */
    @TableField("menu_id")
    private Long menuId;
}
