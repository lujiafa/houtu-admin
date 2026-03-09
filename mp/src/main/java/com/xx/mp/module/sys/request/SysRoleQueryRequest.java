package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.form.PageForm;
import lombok.Data;

@Data
public class SysRoleQueryRequest extends PageForm {

    private String roleName;

    private Integer status;
}
