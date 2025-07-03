package com.houtu.mp.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class SimpleUser extends User {
    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 是否超级管理员
     */
    private boolean admin;

    /**
     * 是否启用MFA
     */
    private boolean enableMFA;
    /**
     * 是否通过MFA验证
     */
    private boolean mfaPassed;

    private final Map<String, Object> attrs = new HashMap<>();

    public SimpleUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.admin = authorities.parallelStream().anyMatch(grantedAuthority -> SecuritySupport.ROLE_ADMIN_NAME.equalsIgnoreCase(grantedAuthority.getAuthority()));
    }

    public SimpleUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.admin = authorities.parallelStream().anyMatch(grantedAuthority -> SecuritySupport.ROLE_ADMIN_NAME.equalsIgnoreCase(grantedAuthority.getAuthority()));
    }

}
