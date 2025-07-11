package com.houtu.mp.module.sys.vo;

import com.houtu.web.model.BaseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysDictSimpleVO extends BaseVO {

    /**
     * 字典类型名称
     */
    private String typeName;

    /**
     * 字典类型编码
     */
    private String typeCode;

    private String typeDesc;

   private List<SysDictDataSimpleVO> list = new ArrayList<>();
}
