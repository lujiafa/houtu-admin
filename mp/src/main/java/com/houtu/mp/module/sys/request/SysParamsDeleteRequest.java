package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.form.PageForm;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysParamsDeleteRequest extends PageForm {

    @NotEmpty(message = "paramIds can't be empty")
    private List<Long> paramIds;
}
