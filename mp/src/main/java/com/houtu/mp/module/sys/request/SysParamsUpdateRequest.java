package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysParamsUpdateRequest extends BaseForm {

    @NotNull(message = "paramId cannot be null")
    private Long paramId;

    @NotBlank(message = "paramName cannot be empty")
    @Length(min = 1, max = 32, message = "paramName length must be between 1 and 32 characters")
    private String paramName;
    /**
     * 参数的数据类型(boolean/int/string/json)
     */
    private String dataType;

    @Length(max = 32, message = "paramValue length must be between 0 and 512 characters")
    private String paramValue;

    private String paramDesc;
}
