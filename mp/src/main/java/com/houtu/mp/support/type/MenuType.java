package com.houtu.mp.support.type;

public enum MenuType {

    DIRECTORY(1, "Directory"),

    MENU(2, "Menu"),

    BUTTON(2, "Function/Button");

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
}
