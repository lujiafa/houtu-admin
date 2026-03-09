package com.xx.mp.module.sys.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysPostQueryBaseVO implements Serializable {

    private Long postId;

    private String postCode;

    private String postName;
}