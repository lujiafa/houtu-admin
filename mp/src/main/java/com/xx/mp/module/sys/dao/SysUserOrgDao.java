package com.xx.mp.module.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.mp.module.sys.entity.SysUserOrgEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * sys_user_org Mapper 接口
 *
 * @author houtu
 * @since 2024-06-24
 */
@Mapper
public interface SysUserOrgDao extends BaseMapper<SysUserOrgEntity> {

    List<Map<Long, String>> selectUserOrgByUserId(@Param("userId") Long userId);

}
