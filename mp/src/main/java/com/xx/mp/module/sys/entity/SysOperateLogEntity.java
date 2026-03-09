package com.xx.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_operate_log
*
* @author jonlu
* @since 2024-07-24
*/
@Getter
@Setter
@TableName("sys_operate_log")
public class SysOperateLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "operate_id", type = IdType.AUTO)
    private Long operateId;

    /**
    * 模块名称
    */
    @TableField("module_name")
    private String moduleName;

    /**
    * 操作类型
    */
    @TableField("operate_type")
    private String operateType;

    /**
    * 请求方法
    */
    @TableField("request_method")
    private String requestMethod;

    /**
    * 请求用户代理
    */
    @TableField("user_agent")
    private String userAgent;

    /**
    * 操作接口方法
    */
    @TableField("method")
    private String method;

    /**
    * 用户ID
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 操作位置IP
    */
    @TableField("ip")
    private String ip;

    /**
    * 基于IP地址的地理位置
    */
    @TableField("location")
    private String location;

    /**
    * 操作参数
    */
    @TableField("request_params")
    private String requestParams;

    /**
    * 响应数据
    */
    @TableField("response_data")
    private String responseData;

    /**
    * 耗时（毫秒）
    */
    @TableField("cost_time")
    private Long costTime;

    /**
    * 操作完成时间
    */
    @TableField("create_time")
    private LocalDateTime createTime;
}
