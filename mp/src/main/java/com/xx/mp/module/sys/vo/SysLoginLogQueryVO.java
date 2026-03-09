package com.xx.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lujiafa.houtu.web.model.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysLoginLogQueryVO extends BaseVO {

    private Long loginLogId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 基于IP地址的地理位置
     */
    private String location;

    /**
     * 登录UA
     */
    private String userAgent;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 登录类型 1-登录 2-登出
     */
    private Integer loginType;

    /**
     * 登录类型描述
     */
    private String loginTypeDesc;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}