package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysUserResetSecretRequest extends BaseForm {

    @NotNull(message = "userId can't be null")
    private Long userId;
}
