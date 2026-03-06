package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class SysRoleUpdateRequest extends BaseForm {

    @NotNull(message = "roleId cannot be null")
    private Long roleId;

    @NotBlank(message = "roleName cannot be empty")
    @Length(min = 2, max = 32, message = "roleName length must be between 2 and 32 characters")
    private String roleName;

    @NotBlank(message = "rolePerms cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{1,32}$", message = "rolePerms format with \"a-zA-Z0-9_-\"，length range 2-32 characters")
    private String rolePerms;

    @NotNull(message = "sort cannot be null")
    private Integer sort;

    @NotNull(message = "status cannot be null")
    private Integer status;

    private String remark;

    private List<Long> menuIds;

}
