package com.houtu.mp.config.security;

public final class SecuritySupport {


    public static final String ROLE_ADMIN_NAME = "ADMIN";

    /**
     * 是否为超级管理员
     * @param originRolePerms 原角色权限编码
     * @return boolean
     */
    public static boolean hasAdmin(String originRolePerms) {
        return ROLE_ADMIN_NAME.equalsIgnoreCase(originRolePerms);
    }


}
