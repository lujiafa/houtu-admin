package com.xx.mp.module.sys.dao;

import com.xx.mp.module.sys.entity.SysUserRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * sys_user_role Mapper 接口
 *
 * @author houtu
 * @since 2024-06-24
 */
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {

    List<Map<Long, String>> selectUserRoleByUserId(@Param("userId") Long userId);

}
