package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysDictAddRequest extends BaseForm {

    @NotBlank(message = "typeCode cannot be empty")
    @Pattern(regexp = "^[\\.a-zA-Z0-9_-]{1,64}$", message = "typeCode format with \".a-zA-Z0-9_-\"，length range 1-64 characters")
    private String typeCode;

    @NotBlank(message = "typeName cannot be empty")
    @Length(min = 1, max = 64, message = "typeName length must be between 1 and 64 characters")
    private String typeName;

    @Length(max = 128, message = "typeDesc length must be between 0 and 128 characters")
    private String typeDesc;

    @NotNull(message = "status cannot be null")
    private Integer status;
}
