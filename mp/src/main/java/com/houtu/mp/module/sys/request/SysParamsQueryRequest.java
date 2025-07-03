package com.houtu.mp.module.sys.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.houtu.web.model.form.PageForm;
import lombok.Data;

@Data
public class SysParamsQueryRequest extends PageForm {

    private String paramCode;

    private String paramName;

    private String dataType;
}
