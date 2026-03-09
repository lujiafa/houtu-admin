package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysRoleDeleteRequest extends BaseForm {

    @NotEmpty(message = "roleIds can't be empty")
    private List<Long> roleIds;

}
