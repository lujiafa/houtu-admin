package com.houtu.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class SysUserQueryVO implements Serializable {

    private Long userId;

    private String userName;

    private String nickName;

    private String email;

    private String phone;

    private String avatar;

    private String lastLoginIp;

    private LocalDateTime lastLoginTime;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private boolean admin;

    private List<Long> orgIds = Collections.emptyList();

    private List<Long> postIds = Collections.emptyList();

    private List<Long> roleIds = Collections.emptyList();

}