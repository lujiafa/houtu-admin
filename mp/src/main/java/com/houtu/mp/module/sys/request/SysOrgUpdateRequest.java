package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysOrgUpdateRequest extends BaseForm {

    @NotNull(message = "orgId cannot be null")
    private Long orgId;

    @NotNull(message = "parentId cannot be null")
    private Long parentId;

    @NotBlank(message = "orgName cannot be empty")
    @Length(min = 2, max = 32, message = "orgName length must be between 2 and 32 characters")
    private String orgName;

    @NotNull(message = "sort cannot be null")
    private Integer sort;

    private String phone;

    private String email;

    @NotNull(message = "status cannot be null")
    private Integer status;

}
