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
* sys_org
*
* @author houtu
* @since 2024-06-24
*/
@Getter
@Setter
@TableName("sys_org")
public class SysOrgEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 部门ID
    */
    @TableId(value = "org_id", type = IdType.AUTO)
    private Long orgId;

    /**
    * 父部门ID
    */
    @TableField("parent_id")
    private Long parentId;

    /**
    * 部门名称
    */
    @TableField("org_name")
    private String orgName;

    /**
    * 显示顺序
    */
    @TableField("sort")
    private Integer sort;

    /**
    * 联系电话
    */
    @TableField("phone")
    private String phone;

    /**
    * 邮箱
    */
    @TableField("email")
    private String email;

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
