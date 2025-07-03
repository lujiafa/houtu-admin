package com.houtu.mp.module.sys.vo;

import com.houtu.web.model.BaseVO;
import lombok.Data;

@Data
public class SysDictDataSimpleVO extends BaseVO {

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

}
