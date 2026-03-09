package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysDictDataQueryRequest extends BaseForm {

    @NotNull(message = "dictId cannot be null")
    private Long dictId;

    private String dictDataName;

    private Integer status;
}
