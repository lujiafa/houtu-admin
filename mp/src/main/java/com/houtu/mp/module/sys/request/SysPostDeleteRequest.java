package com.houtu.mp.module.sys.request;

import com.houtu.web.model.form.PageForm;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysPostDeleteRequest extends PageForm {

    @NotEmpty(message = "postIds can't be empty")
    private List<Long> postIds;
}
