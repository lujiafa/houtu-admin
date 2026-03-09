package com.xx.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
* sys_post_role
*
* @author houtu
* @since 2024-06-24
*/
@Getter
@Setter
@TableName("sys_post_role")
public class SysPostRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 用户ID
    */
    @TableField("post_id")
    private Long postId;

    /**
    * 角色ID
    */
    @TableField("role_id")
    private Long roleId;
}
