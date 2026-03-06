package com.houtu.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lujiafa.houtu.web.model.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysParamsQueryVO extends BaseVO {

    private Long paramId;

    /**
     * 参数代码
     */
    private String paramCode;

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数描述
     */
    private String paramDesc;

    /**
     * 参数的数据类型(boolean/int/string/json)
     */
    private String dataType;

    private String paramValue;

    /**
     * 创建者ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新者ID
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
