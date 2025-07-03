package com.houtu.mp.module.sys.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysOrgQueryBaseVO<T extends SysOrgQueryBaseVO> implements Serializable {

    private Long orgId;

    private Long parentId;

    private String orgName;

    private List<T> children;

}