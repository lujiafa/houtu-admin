package com.houtu.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.houtu.web.model.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysOperateLogQueryVO extends BaseVO {

    private Long operateId;

    private String moduleName;

    private String operateType;

    private String requestMethod;

    private String userAgent;

    private String method;

    private Long userId;

    private String ip;

    private String location;

    private String requestParams;

    private String responseData;

    private Long costTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}