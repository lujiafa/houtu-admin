package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysDictDataAddRequest extends BaseForm {

    @NotNull(message = "dictId cannot be null")
    private Long dictId;

    @NotBlank(message = "dictDataName cannot be empty")
    @Length(min = 1, max = 64, message = "dictDataName length must be between 1 and 64 characters")
    private String dictDataName;

    @NotBlank(message = "dictDataValue cannot be empty")
    @Length(min = 1, max = 64, message = "dictDataValue length must be between 1 and 64 characters")
    private String dictDataValue;

    @Length(max = 128, message = "dictDataDesc length must be between 0 and 128 characters")
    private String dictDataDesc;

    @NotNull(message = "status cannot be null")
    private Integer status;

    private Integer sort;
}
