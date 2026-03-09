package com.xx.mp.module.sys.vo;

import io.github.lujiafa.houtu.web.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SysOrgQueryBaseVO<T extends SysOrgQueryBaseVO> extends BaseVO {

    private Long orgId;

    private Long parentId;

    private String orgName;

    private List<T> children;

}