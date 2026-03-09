package com.xx.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.BaseForm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SysPostAuthorizeRequest extends BaseForm {

    @NotNull(message = "orgId cannot be null")
    private Long postId;

    private List<Long> roleIds;

}
