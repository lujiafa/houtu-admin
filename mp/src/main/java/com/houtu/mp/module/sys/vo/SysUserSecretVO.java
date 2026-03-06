package com.houtu.mp.module.sys.vo;

import io.github.lujiafa.houtu.web.model.BaseVO;
import lombok.Data;

@Data
public class SysUserSecretVO extends BaseVO {

    private String password;

    private String otpImgBase64;
}
