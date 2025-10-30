package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysDictUpdateRequest extends BaseForm {

    @NotNull(message = "dictId cannot be null")
    private Long dictId;

    @NotBlank(message = "typeName cannot be empty")
    @Length(min = 1, max = 64, message = "typeName length must be between 1 and 64 characters")
    private String typeName;

    @Length(max = 128, message = "typeDesc length must be between 0 and 128 characters")
    private String typeDesc;

    @NotNull(message = "status cannot be null")
    private Integer status;
}
