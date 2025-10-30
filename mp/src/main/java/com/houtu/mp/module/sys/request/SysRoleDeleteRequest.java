package com.houtu.mp.module.sys.request;

import com.houtu.web.model.BaseForm;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysRoleDeleteRequest extends BaseForm {

    @NotEmpty(message = "roleIds can't be empty")
    private List<Long> roleIds;

}
