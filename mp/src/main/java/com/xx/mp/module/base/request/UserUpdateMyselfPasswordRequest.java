package com.xx.mp.module.base.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateMyselfPasswordRequest extends BaseForm {

    @NotBlank(message = "oldPassword cannot be empty")
    @Pattern(regexp = "^[-_a-zA-Z\\d\\W]{8,128}$", message = "oldPassword format with \"a-zA-Z0-9_-!@#$%...\"，length range 8-128 characters")
    private String oldPassword;

    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^[-_a-zA-Z\\d\\W]{8,128}$", message = "password format with \"a-zA-Z0-9_-!@#$%...\"，length range 8-128 characters")
    private String password;

}
