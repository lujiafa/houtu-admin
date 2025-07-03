package com.houtu.mp.module.base.mfa;

import com.houtu.mp.module.sys.entity.SysUserEntity;
import com.houtu.web.model.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface MFAProcessor {

    MFAType getMfaType();

    /**
     * 向用户推送MFA信息
     * @param sysUserEntity
     * @return 推送结果
     */
    ResponseData push(SysUserEntity sysUserEntity);

    ResponseData verify(SysUserEntity sysUserEntity, String code);


    @AllArgsConstructor
    @Getter
    public static class MFAType {

        /**
         * MFA类型名称
         */
        private String mfaTypeName;
        /**
         * MFA类型
         */
        private String mfaType;
        /**
         * 是否支持推送/是否需要发送
         */
        private boolean send;

    }
}
