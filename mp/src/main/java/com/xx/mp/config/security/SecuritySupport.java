package com.xx.mp.config.security;

public final class SecuritySupport {


    public static final String ROLE_ADMIN_NAME = "ADMIN";

    /**
     * 是否为超级管理员
     * @param rolePerms 原角色权限编码
     * @return boolean
     */
    public static boolean hasAdmin(String rolePerms) {
        return ROLE_ADMIN_NAME.equalsIgnoreCase(rolePerms);
    }


}
