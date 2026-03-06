package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class SysRoleAddRequest extends BaseForm {

    @NotBlank(message = "roleName cannot be empty")
    @Length(min = 2, max = 32, message = "roleName length must be between 2 and 32 characters")
    private String roleName;

    @NotBlank(message = "rolePerms cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{2,32}$", message = "rolePerms format with \"a-zA-Z0-9_-\"，length range 2-32 characters")
    private String rolePerms;

    @NotNull(message = "sort cannot be null")
    private Integer sort;

    @NotNull(message = "status cannot be null")
    private Integer status;

    private String remark;

    private List<Long> menuIds;

}
