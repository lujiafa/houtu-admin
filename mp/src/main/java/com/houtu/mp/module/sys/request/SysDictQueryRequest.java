package com.houtu.mp.module.sys.request;

import com.houtu.web.model.form.PageForm;
import lombok.Data;

@Data
public class SysDictQueryRequest extends PageForm {

    private String typeCode;

    private String typeName;

    private Integer status;
}
