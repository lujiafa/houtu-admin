package com.xx.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* sys_role
*
* @author houtu
* @since 2024-06-24
*/
@Getter
@Setter
@TableName("sys_role")
public class SysRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 角色ID
    */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
    * 角色名称
    */
    @TableField("role_name")
    private String roleName;

    /**
    * 角色编码/权限编码
    */
    @TableField("role_perms")
    private String rolePerms;

    /**
    * 显示顺序
    */
    @TableField("sort")
    private Integer sort;

    /**
    * 备注
    */
    @TableField("remark")
    private String remark;

    /**
    * 部门状态 0-停用 1-启用/正常
    */
    @TableField("`status`")
    private Integer status;

    /**
    * 删除标志 0-未删除 1-已删除
    */
    @TableField("deleted")
    private Integer deleted;

    /**
    * 创建者ID
    */
    @TableField("create_by")
    private Long createBy;

    /**
    * 创建时间
    */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
    * 更新者ID
    */
    @TableField("update_by")
    private Long updateBy;

    /**
    * 更新时间
    */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
