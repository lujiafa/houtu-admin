package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysDictDataQueryRequest extends BaseForm {

    @NotNull(message = "dictId cannot be null")
    private Long dictId;

    private String dictDataName;

    private Integer status;
}
