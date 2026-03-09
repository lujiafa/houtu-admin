package com.xx.mp.support.type;

import java.util.Arrays;

public enum MenuIconType {

    NATIVE(1, "native"),

    ICONIFY(2, "iconify");

    private Integer iconType;
    private String iconTypeDesc;

    MenuIconType(Integer iconType, String iconTypeDesc) {
        this.iconType = iconType;
        this.iconTypeDesc = iconTypeDesc;
    }

    public Integer getIconType() {
        return iconType;
    }

    public String getIconTypeDesc() {
        return iconTypeDesc;
    }

    public boolean equals(Integer iconType) {
        return this.iconType.equals(iconType);
    }

    public static boolean contains(Integer iconType) {
        if (iconType == null)
            return false;
        return Arrays.stream(values()).parallel().anyMatch(p -> p.equals(iconType));
    }
}
