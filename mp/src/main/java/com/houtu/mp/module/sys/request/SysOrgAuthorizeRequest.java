package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SysOrgAuthorizeRequest extends BaseForm {

    @NotNull(message = "orgId cannot be null")
    private Long orgId;

    private List<Long> roleIds;

}
