package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.form.PageForm;
import lombok.Data;

@Data
public class SysParamsQueryRequest extends PageForm {

    private String paramCode;

    private String paramName;

    private String dataType;
}
