package com.xx.mp.support.type;

public enum ModuleType {

    MENU("Menu"),

    ORG("Org"),

    POST("Post"),

    ROLE("Role"),

    USER("User");

    private String moduleName;

    ModuleType(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }
}
