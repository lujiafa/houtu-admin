package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysPostUpdateRequest extends BaseForm {

    @NotNull(message = "postId cannot be null")
    private Long postId;

    @NotBlank(message = "postCode cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_]{2,32}$", message = "postCode format with \"a-zA-Z0-9_\"，length range 2-32 characters")
    private String postCode;

    @NotBlank(message = "postName cannot be empty")
    @Length(min = 2, max = 32, message = "postName length must be between 2 and 32 characters")
    private String postName;

    @NotNull(message = "status cannot be null")
    private Integer sort;

    @NotNull(message = "status cannot be null")
    private Integer status;

    private String remark;
}
