package com.xx.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_dict_data
*
* @author jonlu
* @since 2024-09-06
*/
@Getter
@Setter
@TableName("sys_dict_data")
public class SysDictDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("dict_data_id")
    private Long dictDataId;

    /**
    * 字典ID
    */
    @TableField("dict_id")
    private Long dictId;

    /**
    * 字典数据名称
    */
    @TableField("dict_data_name")
    private String dictDataName;

    /**
    * 字典数据值
    */
    @TableField("dict_data_value")
    private String dictDataValue;

    /**
    * 字典值描述
    */
    @TableField("dict_data_desc")
    private String dictDataDesc;

    /**
    * 字典顺序
    */
    @TableField("sort")
    private Integer sort;

    /**
    * 部门状态 0-停用 1-启用/正常
    */
    @TableField("`status`")
    private Integer status;

    /**
    * 删除标志 0-未删除 1-已删除
    */
    @TableField("deleted")
    private Integer deleted;

    /**
    * 创建者ID
    */
    @TableField("create_by")
    private Long createBy;

    /**
    * 创建时间
    */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
    * 更新者ID
    */
    @TableField("update_by")
    private Long updateBy;

    /**
    * 更新时间
    */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
