package com.xx.mp.module.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lujiafa.houtu.web.model.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysDictDataQueryVO extends BaseVO {

    private Long dictDataId;

    /**
     * 字典ID
     */
    private Long dictId;

    /**
     * 字典数据名称
     */
    private String dictDataName;

    /**
     * 字典数据值
     */
    private String dictDataValue;

    /**
     * 字典值描述
     */
    private String dictDataDesc;

    /**
     * 字典顺序
     */
    private Integer sort;

    /**
     * 部门状态 0-停用 1-启用/正常
     */
    private Integer status;

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
