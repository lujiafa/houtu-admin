package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.form.PageForm;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysDictDataDeleteRequest extends PageForm {

    @NotEmpty(message = "dictDataIds can't be empty")
    private List<Long> dictDataIds;
}
