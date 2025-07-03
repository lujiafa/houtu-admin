package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.module.sys.dao.SysMenuDao;
import com.houtu.mp.module.sys.dao.SysRoleDao;
import com.houtu.mp.module.sys.entity.SysMenuEntity;
import com.houtu.mp.module.sys.request.SysMenuAddRequest;
import com.houtu.mp.module.sys.request.SysMenuQueryRequest;
import com.houtu.mp.module.sys.request.SysMenuUpdateRequest;
import com.houtu.mp.module.sys.service.SysMenuService;
import com.houtu.mp.module.sys.vo.SysMenuQueryBaseVO;
import com.houtu.mp.module.sys.vo.SysMenuQueryVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.web.model.response.ResponseData;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * sys_menu 服务实现类
 * </p>
 *
 * @author houtu
 * @since 2024-06-21
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public List<SysMenuQueryBaseVO> queryBaseList(SysMenuQueryRequest request) {
        return getChildren(0L, queryListCommon(request), SysMenuQueryBaseVO.class);
    }

    @Override
    public List<SysMenuQueryVO> queryList(SysMenuQueryRequest request) {
        return getChildren(0L, queryListCommon(request), SysMenuQueryVO.class);
    }

    private Map<Long, List<SysMenuEntity>> queryListCommon(SysMenuQueryRequest request) {
        List<SysMenuEntity> sysMenuEntities = baseMapper.selectList(new QueryWrapper<SysMenuEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getMenuName()), "menu_name", request.getMenuName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByAsc("sort"));
        Map<Long, List<SysMenuEntity>> menuParentEntityMap = sysMenuEntities.stream().collect(Collectors.groupingBy(SysMenuEntity::getParentId));
        return menuParentEntityMap;
    }

    @Override
    public List<SysMenuQueryBaseVO> selectBaseListByRoles(List<Long> roleIds, Integer status) {
        List<SysMenuEntity> entityList = baseMapper.queryMenuByRoleIds(roleIds, status, null);
        Map<Long, List<SysMenuEntity>> menuParentEntityMap = entityList.stream().collect(Collectors.groupingBy(SysMenuEntity::getParentId));
        return getChildren(0L, menuParentEntityMap, SysMenuQueryBaseVO.class);
    }

    @Override
    public ResponseData save(SysMenuAddRequest request) {
        if (request.getParentId() != 0
                && baseMapper.selectOne(new QueryWrapper<SysMenuEntity>()
                .eq("menu_id", request.getParentId())
                .eq("deleted", 0)
                .last("limit 1")) == null) {
            return ResponseData.fail(ErrorCode.build(7, LocaleContextHolder.getLocale()));
        }
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        sysMenuEntity.setMenuName(request.getMenuName());
        sysMenuEntity.setParentId(request.getParentId());
        sysMenuEntity.setMenuType(request.getMenuType());
        sysMenuEntity.setSort(request.getSort());
        sysMenuEntity.setStatus(request.getStatus());
        if (request.getMenuType() == 1 || request.getMenuType() == 2) {
            sysMenuEntity.setIconType(request.getIconType());
            sysMenuEntity.setIcon(request.getIcon());
        }
        if (request.getMenuType() == 2) {
            sysMenuEntity.setPathType(request.getPathType());
            sysMenuEntity.setPath(request.getPath());
        }
        if (request.getMenuType() == 2 || request.getMenuType() == 3) {
            ResponseData responseData = verifyPerms(request.getPerms());
            if (!responseData.hasSuccess())
                return responseData;
            sysMenuEntity.setPerms(request.getPerms());
        }
        sysMenuEntity.setCreateBy(SessionContext.getSessionUserId());
        sysMenuEntity.setCreateTime(LocalDateTime.now());
        baseMapper.insert(sysMenuEntity);
        return ResponseData.success();
    }

    @Override
    public ResponseData update(SysMenuUpdateRequest request) {
        SysMenuEntity queryMenuEntity = baseMapper.selectOne(new QueryWrapper<SysMenuEntity>()
                .eq("menu_id", request.getMenuId())
                .eq("deleted", 0)
                .last("limit 1"));
        if (queryMenuEntity == null) {
            return ResponseData.fail(ErrorCode.build(7, LocaleContextHolder.getLocale()));
        }
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        sysMenuEntity.setMenuId(request.getMenuId());
        sysMenuEntity.setMenuName(request.getMenuName());
        sysMenuEntity.setParentId(request.getParentId());
        sysMenuEntity.setSort(request.getSort());
        sysMenuEntity.setStatus(request.getStatus());
        if (queryMenuEntity.getMenuType() == 1 || queryMenuEntity.getMenuType() == 2) {
            sysMenuEntity.setIconType(request.getIconType());
            sysMenuEntity.setIcon(request.getIcon());
        }
        if (queryMenuEntity.getMenuType() == 2) {
            sysMenuEntity.setPathType(request.getPathType());
            sysMenuEntity.setPath(request.getPath());
        }
        if (queryMenuEntity.getMenuType() == 2 || queryMenuEntity.getMenuType() == 3) {
            ResponseData responseData = verifyPerms(request.getPerms());
            if (!responseData.hasSuccess())
                return responseData;
            sysMenuEntity.setPerms(request.getPerms());
        }
        sysMenuEntity.setUpdateBy(SessionContext.getSessionUserId());
        sysMenuEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(sysMenuEntity);
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }

    @Override
    public ResponseData delete(Long menuId) {
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        sysMenuEntity.setMenuId(menuId);
        sysMenuEntity.setDeleted(1);
        sysMenuEntity.setUpdateTime(LocalDateTime.now());
        sysMenuEntity.setCreateBy(SessionContext.getSessionUserId());
        int num = baseMapper.updateById(sysMenuEntity);
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }


    /**
     * 根据父id获取子菜单列表递归方法
     *
     * @param parentId
     * @param menuParentEntityMap
     * @param clazz
     * @param <T>
     */
    private <T extends SysMenuQueryBaseVO> List<T> getChildren(Long parentId, Map<Long, List<SysMenuEntity>> menuParentEntityMap, Class<T> clazz) {
        List<SysMenuEntity> entityList = menuParentEntityMap.get(parentId);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        List<T> menuList = new ArrayList<>();
        entityList.stream().forEach(m -> {
            try {
                SysMenuQueryBaseVO t = clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(m, t);
                t.setChildren(getChildren(m.getMenuId(), menuParentEntityMap, clazz));
                menuList.add((T) t);
            } catch (Exception e) {
            }
        });
        return (List<T>) menuList;
    }

    /**
     * 校验权限及其格式
     * @param perms 权限
     * @return 校验结果
     */
    private ResponseData verifyPerms(String perms) {
        if (StringUtils.isEmpty(perms)) {
            return ResponseData.fail(ErrorCode.build(30, Stream.of("perms can't be empty").toArray()));
        }
        if (!perms.matches("^[a-zA-Z0-9_]+(:[a-zA-Z0-9_]+)+$")) {
            return ResponseData.fail(ErrorCode.build(30, Stream.of("perms format error, perms must contain \":\" (e.g., system:user:query)").toArray()));
        }
        return ResponseData.success();
    }

}
