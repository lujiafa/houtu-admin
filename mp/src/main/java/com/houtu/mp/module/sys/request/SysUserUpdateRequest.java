package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class SysUserUpdateRequest extends BaseForm {

    @NotNull(message = "userId cannot be null")
    private Long userId;

    @NotBlank(message = "nickName cannot be empty")
    @Length(min = 2, max = 32, message = "nickName length must be between 2 and 32 characters")
    private String nickName;

    private String email;

    private String phone;

    private String avatar;

    @NotNull(message = "status cannot be null")
    private Integer status;

    @NotEmpty(message = "orgIds cannot be empty")
    private List<Long> orgIds;

    @NotEmpty(message = "postIds cannot be empty")
    private List<Long> postIds;

}
