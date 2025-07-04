package com.houtu.mp.module.sys.dao;

import com.houtu.mp.module.sys.entity.SysUserPostEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * sys_user_post Mapper 接口
 *
 * @author houtu
 * @since 2024-06-24
 */
@Mapper
public interface SysUserPostDao extends BaseMapper<SysUserPostEntity> {

    List<Map<Long, String>> selectUserPostByUserId(@Param("userId") Long userId);

}
