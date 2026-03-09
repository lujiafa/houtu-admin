package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.form.PageForm;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysPostDeleteRequest extends PageForm {

    @NotEmpty(message = "postIds can't be empty")
    private List<Long> postIds;
}
