package com.houtu.mp.module.sys.request;

import com.houtu.web.model.form.PageForm;
import lombok.Data;

@Data
public class SysMenuQueryRequest extends PageForm {

    private String menuName;

    private Integer status;
}
