package com.xx.mp.module.base.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MFASendRequest extends BaseForm {

    @NotNull(message = "MFA Type can't be null")
    private String mfaType;
}
