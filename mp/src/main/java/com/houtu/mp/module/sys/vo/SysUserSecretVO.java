package com.houtu.mp.module.sys.vo;

import com.houtu.web.model.BaseVO;
import lombok.Data;

@Data
public class SysUserSecretVO extends BaseVO {

    private String password;

    private String otpImgBase64;
}
