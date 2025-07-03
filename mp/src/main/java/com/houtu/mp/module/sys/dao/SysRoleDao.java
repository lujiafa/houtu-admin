package com.houtu.mp.module.sys.dao;

import com.houtu.mp.module.sys.entity.SysRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sys_role Mapper 接口
 *
 * @author houtu
 * @since 2024-06-24
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    List<SysRoleEntity> queryUserRoleList(@Param("userId") Long userId, @Param("status") Integer roleStatus);

}
