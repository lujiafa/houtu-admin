package com.houtu.mp.module.base.vo;

import com.houtu.web.model.BaseVO;
import lombok.Data;

@Data
public class MFAStateVO extends BaseVO {
    // 系统是否支持MFA
    private boolean supportMFA;
    // 个人是否开启MFA，仅需 supportMFA 为真时，该字段才可能为true
    private boolean myselfMFA;
    // 是否开启OTP，仅需 supportMFA && myselfMFA 为真时，该字段才可能为true
    private boolean otp;
}
