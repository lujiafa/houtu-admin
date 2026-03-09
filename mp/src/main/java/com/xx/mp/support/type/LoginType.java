package com.xx.mp.support.type;

public enum LoginType {

    LOGIN(1, "Login"),

    LOGOUT(2, "Logout");

    private int loginType;
    private String loginTypeDesc;

    LoginType(int loginType, String loginTypeDesc) {
        this.loginType = loginType;
        this.loginTypeDesc = loginTypeDesc;
    }

    public int getLoginType() {
        return loginType;
    }

    public String getLoginTypeDesc() {
        return loginTypeDesc;
    }
}
