package com.houtu.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysRoleQueryVO extends SysRoleQueryBaseVO {

    private Integer sort;

    private Integer status;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private List<Long> menuIds;

}