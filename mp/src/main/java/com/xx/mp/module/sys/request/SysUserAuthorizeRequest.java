package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SysUserAuthorizeRequest extends BaseForm {

    @NotNull(message = "userId cannot be null")
    private Long userId;

    private List<Long> roleIds;

}
