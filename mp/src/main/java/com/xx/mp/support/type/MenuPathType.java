package com.xx.mp.support.type;

import java.util.Arrays;

public enum MenuPathType {

    NATIVE(1, "native"),

    LINK(2, "outer link");

    private Integer pathType;
    private String pathTypeDesc;

    MenuPathType(Integer pathType, String pathTypeDesc) {
        this.pathType = pathType;
        this.pathTypeDesc = pathTypeDesc;
    }

    public Integer getPathType() {
        return pathType;
    }

    public String getPathTypeDesc() {
        return pathTypeDesc;
    }

    public boolean equals(Integer pathType) {
        return this.pathType.equals(pathType);
    }

    public static boolean contains(Integer pathType) {
        if (pathType == null)
            return false;
        return Arrays.stream(values()).parallel().anyMatch(p -> p.equals(pathType));
    }
}
