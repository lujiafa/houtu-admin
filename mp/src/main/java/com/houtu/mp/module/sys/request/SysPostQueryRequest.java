package com.houtu.mp.module.sys.request;

import com.houtu.web.model.form.PageForm;
import lombok.Data;

@Data
public class SysPostQueryRequest extends PageForm {

    private String postCode;

    private String postName;

    private Integer status;
}
