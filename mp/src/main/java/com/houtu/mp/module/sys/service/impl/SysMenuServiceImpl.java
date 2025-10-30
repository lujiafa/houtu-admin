package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.mp.module.sys.dao.SysMenuDao;
import com.houtu.mp.module.sys.dao.SysRoleDao;
import com.houtu.mp.module.sys.entity.SysMenuEntity;
import com.houtu.mp.module.sys.entity.SysRoleEntity;
import com.houtu.mp.module.sys.request.SysMenuAddRequest;
import com.houtu.mp.module.sys.request.SysMenuQueryRequest;
import com.houtu.mp.module.sys.request.SysMenuUpdateRequest;
import com.houtu.mp.module.sys.service.SysMenuService;
import com.houtu.mp.module.sys.vo.SysMenuQueryBaseVO;
import com.houtu.mp.module.sys.vo.SysMenuQueryVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.support.type.CommonStatus;
import com.houtu.mp.support.type.MenuIconType;
import com.houtu.mp.support.type.MenuPathType;
import com.houtu.mp.support.type.MenuType;
import com.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    static final String PERMS_REGEX_PATTERN = "^[a-zA-Z0-9_]+(:[a-zA-Z0-9_]+)+$";

    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public List<SysMenuQueryBaseVO> queryUserObtainedList() {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        List<SysMenuEntity> sysMenuList;
        // 超级管理员拥有所有菜单列表
        if (sessionUser.isAdmin()) {
            sysMenuList = baseMapper.selectList(new QueryWrapper<SysMenuEntity>()
                    .eq("deleted", 0)
                    .eq("status", CommonStatus.ENABLED.getStatus())
                    .orderByAsc("sort"));
        } else {
            sysMenuList = new ArrayList<>();
            List<SysRoleEntity> sysRoleList = sysRoleDao.queryUserRoleList(sessionUser.getUserId(), CommonStatus.ENABLED.getStatus());
            if (sysRoleList != null && !sysRoleList.isEmpty()) {
                List<SysMenuEntity> sysMenuEntities = baseMapper.queryMenuByRoleIds(sysRoleList.parallelStream().map(SysRoleEntity::getRoleId).toList(), CommonStatus.ENABLED.getStatus(), null);
                sysMenuList.addAll(sysMenuEntities);
            }
        }
        Map<Long, List<SysMenuEntity>> menuParentMap = sysMenuList.stream().collect(Collectors.groupingBy(SysMenuEntity::getParentId));
        return getChildren(0L, menuParentMap, SysMenuQueryBaseVO.class);
    }

    @Override
    public List<SysMenuQueryVO> queryList(SysMenuQueryRequest request) {
        List<SysMenuEntity> sysMenuEntities = baseMapper.selectList(new QueryWrapper<SysMenuEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getMenuName()), "menu_name", request.getMenuName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByAsc("sort"));
        Map<Long, List<SysMenuEntity>> menuParentEntityMap = sysMenuEntities.stream().collect(Collectors.groupingBy(SysMenuEntity::getParentId));
        return getChildren(0L, menuParentEntityMap, SysMenuQueryVO.class);
    }

    @Override
    public List<SysMenuQueryBaseVO> selectBaseListByRoles(List<Long> roleIds, Integer status) {
        List<SysMenuEntity> entityList = baseMapper.queryMenuByRoleIds(roleIds, status, null);
        Map<Long, List<SysMenuEntity>> menuParentEntityMap = entityList.stream().collect(Collectors.groupingBy(SysMenuEntity::getParentId));
        return getChildren(0L, menuParentEntityMap, SysMenuQueryBaseVO.class);
    }

    @Override
    public ResponseData save(SysMenuAddRequest request) {
        ResponseData responseData = verifyAddForm(request);
        if (!responseData.hasSuccess())
            return responseData;
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        sysMenuEntity.setMenuName(request.getMenuName());
        sysMenuEntity.setParentId(request.getParentId());
        sysMenuEntity.setMenuType(request.getMenuType());
        sysMenuEntity.setSort(request.getSort());
        sysMenuEntity.setStatus(request.getStatus());
        if (MenuType.DIRECTORY.equals(request.getMenuType())) {
            sysMenuEntity.setIconType(request.getIconType());
            sysMenuEntity.setIcon(request.getIcon());
        } else if (MenuType.MENU.equals(request.getMenuType())) {
            sysMenuEntity.setIconType(request.getIconType());
            sysMenuEntity.setIcon(request.getIcon());
            sysMenuEntity.setPathType(request.getPathType());
            sysMenuEntity.setPath(request.getPath());
            if (MenuPathType.NATIVE.equals(request.getPathType()))
                sysMenuEntity.setPerms(request.getPerms());
        } else if (MenuType.FUNCTION.equals(request.getMenuType())) {
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
        if (queryMenuEntity == null)
            return ResponseData.fail(ErrorCode.build(7));
        ResponseData responseData = verifyUpdateForm(queryMenuEntity, request);
        if (!responseData.hasSuccess())
            return responseData;
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        sysMenuEntity.setMenuId(request.getMenuId());
        sysMenuEntity.setMenuName(request.getMenuName());
        sysMenuEntity.setParentId(request.getParentId());
        sysMenuEntity.setSort(request.getSort());
        sysMenuEntity.setStatus(request.getStatus());
        if (MenuType.DIRECTORY.equals(queryMenuEntity.getMenuType())) {
            sysMenuEntity.setIconType(request.getIconType());
            sysMenuEntity.setIcon(request.getIcon());
        } else if (MenuType.MENU.equals(queryMenuEntity.getMenuType())) {
            sysMenuEntity.setIconType(request.getIconType());
            sysMenuEntity.setIcon(request.getIcon());
            sysMenuEntity.setPathType(request.getPathType());
            sysMenuEntity.setPath(request.getPath());
            if (MenuPathType.NATIVE.equals(request.getPathType()))
                sysMenuEntity.setPerms(request.getPerms());
        } else if (MenuType.FUNCTION.equals(queryMenuEntity.getMenuType())) {
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
     * 校验添加表单
     *
     * @param request 添加表单
     * @return 校验结果
     */
    ResponseData verifyAddForm(@NotNull SysMenuAddRequest request) {
        if (!Objects.equals(request.getParentId(), 0)
                && baseMapper.selectOne(new QueryWrapper<SysMenuEntity>()
                .eq("menu_id", request.getParentId())
                .eq("deleted", 0)
                .last("limit 1")) == null) {
            return ResponseData.fail(ErrorCode.build(7));
        }
        if (!MenuType.contains(request.getMenuType())) {
            return ResponseData.fail(ErrorCode.build(30, Stream.of("menuType is invalid").toArray()));
        }
        if (MenuType.DIRECTORY.equals(request.getMenuType())) {
            if (!MenuIconType.contains(request.getIconType())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("iconType is invalid").toArray()));
            }
            if (StringUtils.isBlank(request.getIcon())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("icon can't be empty").toArray()));
            }
        } else if (MenuType.MENU.equals(request.getMenuType())) {
            if (!MenuIconType.contains(request.getIconType())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("iconType is invalid").toArray()));
            }
            if (StringUtils.isBlank(request.getIcon())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("icon can't be empty").toArray()));
            }
            if (!MenuPathType.contains(request.getPathType())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("pathType is invalid").toArray()));
            }
            if (StringUtils.isBlank(request.getPath())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("path can't be empty").toArray()));
            }
            if (request.getPerms() != null && !request.getPerms().matches(PERMS_REGEX_PATTERN)) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("perms format error, perms must contain \":\" (e.g., system:user:query)").toArray()));
            }
        } else if (MenuType.FUNCTION.equals(request.getMenuType())) {
            if (request.getPerms() == null) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("perms can't be empty").toArray()));
            }
            if (!request.getPerms().matches(PERMS_REGEX_PATTERN)) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("perms format error, perms must contain \":\" (e.g., system:user:query)").toArray()));
            }
        }
        return ResponseData.success();
    }

    /**
     * 验证修改表单
     *
     * @param sysMenuEntity 原始数据
     * @param request       修改表单
     * @return 验证结果
     */
    ResponseData verifyUpdateForm(@NotNull SysMenuEntity sysMenuEntity, @NotNull SysMenuUpdateRequest request) {
        if (request.getParentId() != null
                && !Objects.equals(request.getParentId(), 0)
                && !request.getParentId().equals(sysMenuEntity.getParentId())
                && baseMapper.selectOne(new QueryWrapper<SysMenuEntity>()
                .eq("menu_id", request.getParentId())
                .eq("deleted", 0)
                .last("limit 1")) == null) {
            return ResponseData.fail(ErrorCode.build(7));
        }
        if (MenuType.DIRECTORY.equals(sysMenuEntity.getMenuType())) {
            // iconType 为空时，即不需要修改
            if (request.getIconType() != null && !MenuIconType.contains(request.getIconType())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("iconType is invalid").toArray()));
            }
        } else if (MenuType.MENU.equals(sysMenuEntity.getMenuType())) {
            // iconType 为空时，即不需要修改
            if (request.getIconType() != null && !MenuIconType.contains(request.getIconType())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("iconType is invalid").toArray()));
            }
            // pathType 为空时，即不需要修改
            if (request.getPathType() != null && !MenuPathType.contains(request.getPathType())) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("pathType is invalid").toArray()));
            }
            if (request.getPerms() != null && !request.getPerms().matches(PERMS_REGEX_PATTERN)) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("perms format error, perms must contain \":\" (e.g., system:user:query)").toArray()));
            }
        } else if (MenuType.FUNCTION.equals(sysMenuEntity.getMenuType())) {
            if (request.getPerms() == null) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("perms can't be empty").toArray()));
            }
            if (!request.getPerms().matches(PERMS_REGEX_PATTERN)) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("perms format error, perms must contain \":\" (e.g., system:user:query)").toArray()));
            }
        }
        return ResponseData.success();
    }

}
