package com.houtu.mp.support.type;

public enum OperateType {

    ADD("Add"),

    UPDATE("Update"),

    DELETE("Delete"),

    AUTHORIZE("Authorize");

    private String operateType;

    OperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateType() {
        return operateType;
    }
}
