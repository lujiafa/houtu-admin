package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.form.PageForm;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysDictDeleteRequest extends PageForm {

    @NotEmpty(message = "dictIds can't be empty")
    private List<Long> dictIds;
}
