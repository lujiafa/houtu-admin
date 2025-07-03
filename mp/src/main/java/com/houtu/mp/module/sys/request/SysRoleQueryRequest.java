package com.houtu.mp.module.sys.request;

import com.houtu.web.model.form.PageForm;
import lombok.Data;

@Data
public class SysRoleQueryRequest extends PageForm {

    private String roleName;

    private Integer status;
}
