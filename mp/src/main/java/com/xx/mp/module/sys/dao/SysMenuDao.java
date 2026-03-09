package com.xx.mp.module.sys.dao;

import com.xx.mp.module.sys.entity.SysMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sys_menu Mapper 接口
 *
 * @author houtu
 * @since 2024-06-21
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

    /**
     * 根据角色查询菜单列表
     * @param roleIds 角色ID集合
     * @param status 菜单状态，可省略
     * @return 集合
     */
    List<SysMenuEntity> queryMenuByRoleIds(@Param("roleIds") List<Long> roleIds, @Param("status") Integer status, @Param("menuTypes") List<Integer> menuTypes);

    /**
     * 根据ID修改菜单
     * @param sysMenuEntity 菜单对象
     * @return 修改结果行数
     */
    int updateById(SysMenuEntity sysMenuEntity);
}
