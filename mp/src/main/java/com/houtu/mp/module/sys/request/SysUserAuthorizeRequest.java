package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SysUserAuthorizeRequest extends BaseForm {

    @NotNull(message = "userId cannot be null")
    private Long userId;

    private List<Long> roleIds;

}
