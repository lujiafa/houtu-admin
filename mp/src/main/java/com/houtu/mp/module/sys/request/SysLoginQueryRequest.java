package com.houtu.mp.module.sys.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.houtu.web.model.form.PageForm;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysLoginQueryRequest extends PageForm {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginCreateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endCreateTime;
}
