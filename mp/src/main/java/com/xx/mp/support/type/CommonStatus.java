package com.xx.mp.support.type;

public enum CommonStatus {

    ENABLED(1, "正常"),
    DISABLED(0, "禁用");

    private Integer status;
    private String statusDesc;

    CommonStatus(Integer status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}
