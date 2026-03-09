package com.xx.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.xx.mp.config.security.SecuritySupport;
import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.sys.dao.*;
import com.xx.mp.module.sys.entity.*;
import com.xx.mp.module.sys.request.SysRoleAddRequest;
import com.xx.mp.module.sys.request.SysRoleQueryRequest;
import com.xx.mp.module.sys.request.SysRoleUpdateRequest;
import com.xx.mp.module.sys.service.SysRoleService;
import com.xx.mp.module.sys.vo.SysRoleQueryUserOperableVO;
import com.xx.mp.module.sys.vo.SysRoleQueryVO;
import com.xx.mp.support.SessionContext;
import com.xx.mp.support.type.CommonStatus;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * sys_role 服务实现类
 * </p>
 *
 * @author houtu
 * @since 2024-06-24
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {

    @Resource
    private SysUserRoleDao sysUserRoleDao;
    @Resource
    private SysOrgRoleDao sysOrgRoleDao;
    @Resource
    private SysPostRoleDao sysPostRoleDao;
    @Resource
    private SysRoleMenuDao sysRoleMenuDao;

    @Override
    public List<SysRoleQueryUserOperableVO> queryUserOperableList() {
        List<SysRoleEntity> entityList = baseMapper.selectList(new QueryWrapper<SysRoleEntity>()
                .eq("deleted", 0)
                .eq("status", CommonStatus.ENABLED.getStatus())
                .orderByAsc("sort"));
        if (SessionContext.isAdmin()) {
            return entityList.stream().map(entity -> {
                SysRoleQueryUserOperableVO baseVO = new SysRoleQueryUserOperableVO();
                BeanUtils.copyProperties(entity, baseVO);
                return baseVO;
            }).collect(Collectors.toList());
        }
        return entityList.stream().filter(r ->  !SecuritySupport.hasAdmin(r.getRolePerms())).map(entity -> {
            SysRoleQueryUserOperableVO baseVO = new SysRoleQueryUserOperableVO();
            BeanUtils.copyProperties(entity, baseVO);
            return baseVO;
        }).collect(Collectors.toList());
    }

    @Override
    public ResponseData<PageDataVO<SysRoleQueryVO>> pageQuery(SysRoleQueryRequest request) {
        Page<SysRoleEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        page = baseMapper.selectPage(page, new QueryWrapper<SysRoleEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getRoleName()), "role_name", request.getRoleName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByAsc("sort"));
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            List<SysRoleEntity> records = page.getRecords();
            Map<Long, List<SysRoleMenuEntity>> roleMenuMap = sysRoleMenuDao.selectList(new QueryWrapper<SysRoleMenuEntity>()
                            .in("role_id", page.getRecords().stream().map(SysRoleEntity::getRoleId).collect(Collectors.toList())))
                    .stream().collect(Collectors.groupingBy(p -> p.getRoleId()));
            return ResponseData.success(PageDataVO.build(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords().stream().map(p -> {
                SysRoleQueryVO vo = new SysRoleQueryVO();
                BeanUtils.copyProperties(p, vo);
                vo.setAdmin(SecuritySupport.hasAdmin(p.getRolePerms()));
                List<SysRoleMenuEntity> roleMenuEntities = roleMenuMap.get(p.getRoleId());
                if (roleMenuEntities != null) {
                    vo.setMenuIds(roleMenuEntities.stream().map(SysRoleMenuEntity::getMenuId).collect(Collectors.toList()));
                } else {
                    vo.setMenuIds(Collections.emptyList());
                }
                return vo;
            }).collect(Collectors.toList())));
        }
        return ResponseData.success(PageDataVO.empty());
    }

    @Transactional
    @Override
    public ResponseData save(SysRoleAddRequest request) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        if (!sessionUser.isAdmin()) {
            if (SecuritySupport.hasAdmin(request.getRolePerms()))
                return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale(), Stream.of("您暂无添加超级管理角色的权限").toArray()));
        }
        boolean roleHasAdmin = SecuritySupport.hasAdmin(request.getRolePerms());
        SysRoleEntity entity = new SysRoleEntity();
        entity.setRoleName(request.getRoleName());
        entity.setRolePerms(request.getRolePerms());
        entity.setSort(request.getSort());
        entity.setRemark(request.getRemark());
        entity.setStatus(request.getStatus());
        entity.setRolePerms(request.getRolePerms());
        entity.setCreateBy(SessionContext.getSessionUserId());
        entity.setCreateTime(LocalDateTime.now());

        int num = baseMapper.insert(entity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        if (roleHasAdmin)
            return ResponseData.success();

        if (request.getMenuIds() != null
                && request.getMenuIds().size() > 0) {
            Long roleId = entity.getRoleId();
            List<SysRoleMenuEntity> roleMenuEntities = request.getMenuIds().stream().map(m -> {
                SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();
                roleMenuEntity.setMenuId(m);
                roleMenuEntity.setRoleId(roleId);
                return roleMenuEntity;
            }).collect(Collectors.toList());
            sysRoleMenuDao.insert(roleMenuEntities);
        }
        return ResponseData.success();
    }

    @Transactional
    @Override
    public ResponseData update(SysRoleUpdateRequest request) {
        boolean roleHasAdmin = SecuritySupport.hasAdmin(request.getRolePerms());
        SimpleUser sessionUser = SessionContext.getSessionUser();
        if (!sessionUser.isAdmin()) {
            SysRoleEntity sysRoleEntity = baseMapper.selectById(request.getRoleId());
            if (SecuritySupport.hasAdmin(sysRoleEntity.getRolePerms()))
                return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale(), Stream.of("您暂无修改超级管理角色的权限").toArray()));
            if (roleHasAdmin)
                return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale(), Stream.of("您暂无将普通角色修改为超级管理角色的权限").toArray()));
        }

        SysRoleEntity entity = new SysRoleEntity();
        entity.setRoleId(request.getRoleId());
        entity.setRoleName(request.getRoleName());
        entity.setRolePerms(request.getRolePerms());
        entity.setSort(request.getSort());
        entity.setStatus(request.getStatus());
        entity.setRemark(request.getRemark());
        entity.setUpdateBy(SessionContext.getSessionUser().getUserId());
        entity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(entity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4));
        }
        if (roleHasAdmin) {
            // 超管角色无需角色-菜单关联数据
            sysRoleMenuDao.delete(new QueryWrapper<SysRoleMenuEntity>()
                    .eq("role_id", request.getRoleId()));
            return ResponseData.success();
        }

        List<Long> existsMenuIds = sysRoleMenuDao.selectList(new QueryWrapper<SysRoleMenuEntity>().eq("role_id", request.getRoleId())).stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        List<Long> reqMenuIds = request.getMenuIds() == null ? Collections.emptyList() : request.getMenuIds();
        List<Long> delMenuIds = existsMenuIds.stream().filter(p -> !reqMenuIds.contains(p)).collect(Collectors.toList());
        List<Long> addMenuIds = reqMenuIds.stream().filter(p -> !existsMenuIds.contains(p)).collect(Collectors.toList());
        if (delMenuIds.size() > 0) {
            sysRoleMenuDao.delete(new QueryWrapper<SysRoleMenuEntity>()
                    .eq("role_id", request.getRoleId())
                    .in("menu_id", delMenuIds));
        }
        if (addMenuIds.size() > 0) {
            List<SysRoleMenuEntity> roleMenuEntities = addMenuIds.stream().map(m -> {
                SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();
                roleMenuEntity.setMenuId(m);
                roleMenuEntity.setRoleId(request.getRoleId());
                return roleMenuEntity;
            }).collect(Collectors.toList());
            sysRoleMenuDao.insert(roleMenuEntities);
        }
        return ResponseData.success();
    }

    @Transactional
    @Override
    public ResponseData delete(List<Long> roleIds) {
        sysOrgRoleDao.delete(new QueryWrapper<SysOrgRoleEntity>().in("role_id", roleIds));
        sysPostRoleDao.delete(new QueryWrapper<SysPostRoleEntity>().in("role_id", roleIds));
        sysUserRoleDao.delete(new QueryWrapper<SysUserRoleEntity>().in("role_id", roleIds));
        SysRoleEntity entity = new SysRoleEntity();
        entity.setDeleted(1);
        entity.setUpdateBy(SessionContext.getSessionUserId());
        entity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.update(entity, new QueryWrapper<SysRoleEntity>().in("role_id", roleIds));
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }
}
