package com.houtu.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
* sys_user_org
*
* @author houtu
* @since 2024-06-24
*/
@Getter
@Setter
@TableName("sys_user_org")
public class SysUserOrgEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 用户ID
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 岗位ID
    */
    @TableField("org_id")
    private Long orgId;
}
