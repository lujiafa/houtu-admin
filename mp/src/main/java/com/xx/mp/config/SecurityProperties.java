package com.xx.mp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security")
@Data
public class SecurityProperties {

    /** 是否启用MFA支持 **/
    public boolean mfa;

    /**
     * 默认是否启用MFA，仅当mfa=true时有效
     */
    private boolean defaultEnableMFA;

}
