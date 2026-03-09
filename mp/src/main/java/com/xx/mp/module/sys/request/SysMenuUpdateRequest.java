package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysMenuUpdateRequest extends BaseForm {

    @NotNull(message = "menuId不能为空")
    private Long menuId;

    private String menuName;

    private Long parentId;

    private Integer sort;

    private Integer status;

    private Integer iconType;

    private String icon;

    private Integer pathType;

    private String path;

    private String perms;


}
