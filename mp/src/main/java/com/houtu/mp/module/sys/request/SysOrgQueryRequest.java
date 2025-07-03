package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import lombok.Data;

@Data
public class SysOrgQueryRequest extends BaseForm {

    private String orgName;

    private Integer status;
}
