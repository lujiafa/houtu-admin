package com.xx.mp.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.base.service.MenuService;
import com.xx.mp.module.base.vo.ShowMenuVO;
import com.xx.mp.module.sys.dao.SysMenuDao;
import com.xx.mp.module.sys.dao.SysRoleDao;
import com.xx.mp.module.sys.entity.SysMenuEntity;
import com.xx.mp.module.sys.entity.SysRoleEntity;
import com.xx.mp.support.SessionContext;
import com.xx.mp.support.type.MenuType;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    protected SysMenuDao sysMenuDao;
    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public List<ShowMenuVO> selectLoginUserMenuList() {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        List<SysMenuEntity> sysMenuEntities = null;
        if (sessionUser.isAdmin()) {
            sysMenuEntities = sysMenuDao.selectList(new QueryWrapper<SysMenuEntity>()
                    .eq("status", 1)
                    .eq("deleted", 0)
                    .in("menu_type", Arrays.stream(MenuType.values()).map(mt -> mt.getMenuType()).toArray())
                    .orderByAsc("sort"));
        } else {
            List<SysRoleEntity> sysRoleEntityList = sysRoleDao.queryUserRoleList(sessionUser.getUserId(), 1);
            if (sysRoleEntityList == null || sysRoleEntityList.isEmpty()) {
                return List.of();
            }
            sysMenuEntities = sysMenuDao.queryMenuByRoleIds(sysRoleEntityList.parallelStream().map(SysRoleEntity::getRoleId).toList(), 1, Arrays.stream(MenuType.values()).map(mt -> mt.getMenuType()).toList());
            if (sysMenuEntities == null || sysMenuEntities.isEmpty()) {
                return List.of();
            }
        }
        Map<Long, List<SysMenuEntity>> menuParentEntityMap = sysMenuEntities.stream().collect(Collectors.groupingBy(SysMenuEntity::getParentId));
        return getLoginUserChildren(0L, menuParentEntityMap);
    }

    /**
     * 根据父id获取子菜单列表递归方法
     *
     * @param parentId
     * @param menuParentEntityMap
     */
    private List<ShowMenuVO> getLoginUserChildren(Long parentId, Map<Long, List<SysMenuEntity>> menuParentEntityMap) {
        List<SysMenuEntity> entityList = menuParentEntityMap.get(parentId);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        List<ShowMenuVO> menuList = new ArrayList<>();
        entityList.stream().forEach(m -> {
            ShowMenuVO menuVO = new ShowMenuVO();
            BeanUtils.copyProperties(m, menuVO);
            if (MenuType.MENU.equals(m.getMenuType())) {
                List<SysMenuEntity> funMenuList = menuParentEntityMap.get(m.getMenuId());
                List<String> perms = new ArrayList();
                if (StringUtils.isNotEmpty(m.getPerms())) {
                    perms.add(m.getPerms());
                }
                if (funMenuList != null) {
                    perms.addAll(funMenuList.stream().filter(f-> StringUtils.isNotEmpty(f.getPerms())).map(f -> f.getPerms()).collect(Collectors.toList()));
                }
                menuVO.setPerms(perms);
            }
            if (MenuType.DIRECTORY.equals(m.getMenuType())) {
                menuVO.setChildren(getLoginUserChildren(m.getMenuId(), menuParentEntityMap));
            }
            menuList.add(menuVO);
        });
        return menuList;
    }

}
