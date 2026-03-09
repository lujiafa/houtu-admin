package com.xx.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lujiafa.houtu.web.model.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysRoleQueryVO extends BaseVO {

    private Long roleId;

    private String roleName;

    private String rolePerms;

    private boolean admin;

    private Integer sort;

    private Integer status;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private List<Long> menuIds;

}