package com.houtu.mp.module.base.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MFAVerifyRequest extends BaseForm {

    @NotNull(message = "MFA Type can't be null")
    private String mfaType;

    @NotNull(message = "MFA Type can't be null")
    private String code;
}
