package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysParamsAddRequest extends BaseForm {

    @NotBlank(message = "paramCode cannot be empty")
    @Pattern(regexp = "^[\\.a-zA-Z0-9_-]{1,64}$", message = "paramCode format with \".a-zA-Z0-9_-\"，length range 1-64 characters")
    private String paramCode;

    @NotBlank(message = "paramName cannot be empty")
    @Length(min = 1, max = 64, message = "paramName length must be between 1 and 64 characters")
    private String paramName;
    /**
     * 参数的数据类型(boolean/int/string/json)
     */
    private String dataType;

    @Length(max = 512, message = "paramValue length must be between 0 and 512 characters")
    private String paramValue;

    private String paramDesc;
}
