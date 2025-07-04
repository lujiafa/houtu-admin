package com.houtu.mp.support.type;

import java.util.Arrays;

public enum MenuType {

    DIRECTORY(1, "Directory"),

    MENU(2, "Menu"),

    FUNCTION(3, "Function/Button");

    private Integer menuType;
    private String menuTypeDesc;

    MenuType(Integer menuType, String menuTypeDesc) {
        this.menuType = menuType;
        this.menuTypeDesc = menuTypeDesc;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public String getMenuTypeDesc() {
        return menuTypeDesc;
    }

    public boolean equals(Integer menuType) {
        return this.menuType.equals(menuType);
    }

    public static boolean contains(Integer menuType) {
        if (menuType == null)
            return false;
        return Arrays.stream(values()).parallel().anyMatch(p -> p.equals(menuType));
    }
}
