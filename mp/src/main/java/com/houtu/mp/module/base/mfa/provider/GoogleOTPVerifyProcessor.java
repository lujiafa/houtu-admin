package com.houtu.mp.module.base.mfa.provider;

import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.module.base.mfa.MFAProcessor;
import com.houtu.mp.module.sys.entity.SysUserEntity;
import com.houtu.mp.util.OTPUtils;
import com.houtu.web.model.ResponseData;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Google安全验证码
 */
@Component
public class GoogleOTPVerifyProcessor implements MFAProcessor {

    public static final String MFA_TYPE = "GOOGLE_OTP";

    @Override
    public MFAType getMfaType() {
        return new MFAType("Google安全码", MFA_TYPE, false);
    }

    @Override
    public ResponseData push(SysUserEntity sysUserEntity) {
        return ResponseData.success();
    }

    @Override
    public ResponseData verify(SysUserEntity sysUserEntity, String code) {
        Assert.notNull(code, "验证码不能为空");
        if (!OTPUtils.checkCode(sysUserEntity.getGoogleOTPKey(), Integer.valueOf(code))) {
            return ResponseData.fail(ErrorCode.build(17));
        }
        return null;
    }
}
