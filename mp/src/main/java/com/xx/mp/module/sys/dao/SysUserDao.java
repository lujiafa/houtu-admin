package com.xx.mp.module.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.mp.module.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * sys_user Mapper 接口
 *
 * @author houtu
 * @since 2024-06-21
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    Page<SysUserEntity> queryPage(Page<SysUserEntity> page, @Param("params") Map<String, Object> params);

}
