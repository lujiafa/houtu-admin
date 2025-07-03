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
* sys_login_log
*
* @author jonlu
* @since 2024-07-24
*/
@Getter
@Setter
@TableName("sys_login_log")
public class SysLoginLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "login_log_id", type = IdType.AUTO)
    private Long loginLogId;


    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
    * 用户名
    */
    @TableField("user_name")
    private String userName;

    /**
    * 登录IP
    */
    @TableField("ip")
    private String ip;

    /**
    * 基于IP地址的地理位置
    */
    @TableField("location")
    private String location;

    /**
    * 登录UA
    */
    @TableField("user_agent")
    private String userAgent;

    /**
    * 设备类型
    */
    @TableField("device_type")
    private String deviceType;

    /**
    * 登录类型 1-登录 2-登出
    */
    @TableField("login_type")
    private Integer loginType;

    /**
    * 登录类型描述
    */
    @TableField("login_type_desc")
    private String loginTypeDesc;

    /**
    * 操作时间
    */
    @TableField("create_time")
    private LocalDateTime createTime;
}
