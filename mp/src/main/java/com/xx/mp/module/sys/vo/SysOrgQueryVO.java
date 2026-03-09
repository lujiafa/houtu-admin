package com.xx.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysOrgQueryVO extends SysOrgQueryBaseVO {

    private Integer sort;

    private String phone;

    private String email;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}