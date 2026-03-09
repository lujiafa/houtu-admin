package com.xx.mp.module.sys.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysMenuQueryBaseVO<T extends SysMenuQueryBaseVO> implements Serializable {

    private Long menuId;

    private String menuName;

    private Long parentId;

    private List<T> children;

}