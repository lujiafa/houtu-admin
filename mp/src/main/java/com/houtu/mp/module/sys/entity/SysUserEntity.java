package com.houtu.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* sys_user
*
* @author houtu
* @since 2024-06-21
*/
@Getter
@Setter
@TableName("sys_user")
public class SysUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 用户ID
    */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
    * 用户账号
    */
    @TableField("user_name")
    private String userName;

    /**
    * 用户昵称
    */
    @TableField("nick_name")
    private String nickName;

    /**
    * 用户邮箱
    */
    @TableField("email")
    private String email;

    /**
    * 手机号码
    */
    @TableField("phone")
    private String phone;

    /**
    * 头像地址
    */
    @TableField("avatar")
    private String avatar;

    /**
    * 密码
    */
    @TableField("`password`")
    private String password;

    /**
    * 是否启用MFA 0-未启用 1-启用
    */
    @TableField("enable_mfa")
    private Integer enableMFA;

    /**
    * google otp 密钥
    */
    @TableField("google_otp_key")
    private String googleOTPKey;

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
