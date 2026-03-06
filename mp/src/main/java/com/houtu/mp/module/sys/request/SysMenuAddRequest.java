package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysMenuAddRequest extends BaseForm {

    @NotBlank(message = "menuName cannot be empty")
    @Length(min = 2, max = 32, message = "menuName length must be between 2 and 32 characters")
    private String menuName;

    @NotNull(message = "menuType cannot be null")
    private Integer menuType;

    @NotNull(message = "parentId cannot be null")
    private Long parentId;

    @NotNull(message = "sort cannot be null")
    private Integer sort;

    @NotNull(message = "status cannot be null")
    private Integer status;

    private Integer iconType;

    private String icon;

    private Integer pathType;

    private String path;

    /**
     * 权限标识，若存在则必须包含英文":"
     */
    private String perms;


}
