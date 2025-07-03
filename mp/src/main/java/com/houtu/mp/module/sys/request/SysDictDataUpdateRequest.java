package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysDictDataUpdateRequest extends BaseForm {

    @NotNull(message = "dictDataId cannot be null")
    private Long dictDataId;

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
