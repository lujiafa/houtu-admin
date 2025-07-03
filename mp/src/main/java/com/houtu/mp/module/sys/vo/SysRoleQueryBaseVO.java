package com.houtu.mp.module.sys.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleQueryBaseVO implements Serializable {

    private Long roleId;

    private String roleName;

    private String rolePerms;

    private boolean admin;

}