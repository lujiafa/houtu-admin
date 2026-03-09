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
* sys_post
*
* @author houtu
* @since 2024-06-24
*/
@Getter
@Setter
@TableName("sys_post")
public class SysPostEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 岗位ID
    */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    /**
    * 岗位编码
    */
    @TableField("post_code")
    private String postCode;

    /**
    * 岗位名称
    */
    @TableField("post_name")
    private String postName;

    /**
    * 显示顺序
    */
    @TableField("sort")
    private Integer sort;

    /**
    * 状态 0-停用 1-启用/正常
    */
    @TableField("`status`")
    private Integer status;

    /**
    * 备注
    */
    @TableField("remark")
    private String remark;

    /**
    * 删除标志 0-未删除 1-已删除
    */
    @TableField("deleted")
    private Integer deleted;

    /**
    * 创建者
    */
    @TableField("create_by")
    private Long createBy;

    /**
    * 创建时间
    */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
    * 更新者
    */
    @TableField("update_by")
    private Long updateBy;

    /**
    * 更新时间
    */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
