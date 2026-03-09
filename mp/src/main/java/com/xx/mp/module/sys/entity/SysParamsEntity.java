package com.xx.mp.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_params
*
* @author jonlu
* @since 2024-09-06
*/
@Getter
@Setter
@TableName("sys_params")
public class SysParamsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键，自增标识符
    */
    @TableId(value = "param_id", type = IdType.AUTO)
    private Long paramId;

    /**
    * 参数代码
    */
    @TableField("param_code")
    private String paramCode;

    /**
    * 参数名
    */
    @TableField("param_name")
    private String paramName;

    /**
     * 参数值
     */
    @TableField("param_value")
    private String paramValue;

    /**
    * 参数描述
    */
    @TableField("param_desc")
    private String paramDesc;

    /**
    * 参数的数据类型(boolean/int/string/json)
    */
    @TableField("data_type")
    private String dataType;

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
