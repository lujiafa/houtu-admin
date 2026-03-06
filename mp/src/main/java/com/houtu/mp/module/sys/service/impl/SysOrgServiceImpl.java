package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.mp.module.sys.request.SysOrgAuthorizeRequest;
import com.houtu.mp.module.sys.service.SysOrgService;
import com.houtu.mp.support.SessionContext;
import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.houtu.mp.module.sys.dao.SysOrgDao;
import com.houtu.mp.module.sys.dao.SysOrgRoleDao;
import com.houtu.mp.module.sys.entity.SysOrgEntity;
import com.houtu.mp.module.sys.entity.SysOrgRoleEntity;
import com.houtu.mp.module.sys.request.SysOrgAddRequest;
import com.houtu.mp.module.sys.request.SysOrgQueryRequest;
import com.houtu.mp.module.sys.request.SysOrgUpdateRequest;
import com.houtu.mp.module.sys.vo.SysOrgQueryBaseVO;
import com.houtu.mp.module.sys.vo.SysOrgQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * sys_org 服务实现类
 * </p>
 *
 * @author houtu
 * @since 2024-06-24
 */
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgDao, SysOrgEntity> implements SysOrgService {

    @Resource
    private SysOrgRoleDao orgRoleDao;

    @Override
    public List<SysOrgQueryBaseVO> queryBaseList(SysOrgQueryRequest request) {
        return getChildren(0L, queryListCommon(request), SysOrgQueryBaseVO.class);
    }

    @Override
    public List<SysOrgQueryVO> queryList(SysOrgQueryRequest request) {
        return getChildren(0L, queryListCommon(request), SysOrgQueryVO.class);
    }

    @Transactional
    @Override
    public ResponseData save(SysOrgAddRequest request) {
        SysOrgEntity sysOrgEntity = new SysOrgEntity();
        BeanUtils.copyProperties(request, sysOrgEntity);
        sysOrgEntity.setCreateBy(SessionContext.getSessionUserId());
        sysOrgEntity.setCreateTime(LocalDateTime.now());
        int num = baseMapper.insert(sysOrgEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Transactional
    @Override
    public ResponseData update(SysOrgUpdateRequest request) {
        SysOrgEntity orgEntity = new SysOrgEntity();
        BeanUtils.copyProperties(request, orgEntity);
        orgEntity.setUpdateBy(SessionContext.getSessionUserId());
        orgEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(orgEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Transactional
    @Override
    public ResponseData authorize(SysOrgAuthorizeRequest request) {
        List<Long> reqRoleIds = request.getRoleIds() == null ? List.of() : request.getRoleIds();
        List<Long> existsRoleIds = orgRoleDao.selectList(new QueryWrapper<SysOrgRoleEntity>().eq("org_id", request.getOrgId())).stream().map(p -> p.getRoleId()).toList();
        List<Long> addRoleIds = reqRoleIds.stream().filter(o -> !existsRoleIds.contains(o)).toList();
        List<Long> delRoleIds = existsRoleIds.stream().filter(o -> !reqRoleIds.contains(o)).toList();
        if (addRoleIds.size() > 0) {
            orgRoleDao.insert(addRoleIds.stream().map(o -> {
                SysOrgRoleEntity orgRoleEntity = new SysOrgRoleEntity();
                orgRoleEntity.setRoleId(o);
                orgRoleEntity.setOrgId(request.getOrgId());
                return orgRoleEntity;
            }).toList());
        }
        if (delRoleIds.size() > 0) {
            orgRoleDao.delete(new QueryWrapper<SysOrgRoleEntity>().eq("org_id", request.getOrgId()).in("role_id", delRoleIds));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData delete(Long orgId) {
        SysOrgEntity orgEntity = new SysOrgEntity();
        orgEntity.setOrgId(orgId);
        orgEntity.setDeleted(1);
        orgEntity.setUpdateBy(SessionContext.getSessionUserId());
        orgEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(orgEntity);
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }

    /**
     * 根据父id获取子组织机构列表递归方法
     *
     * @param parentId
     * @param orgParentEntityMap
     */
    private <T extends SysOrgQueryBaseVO> List<T> getChildren(Long parentId, Map<Long, List<SysOrgEntity>> orgParentEntityMap, Class<T> clazz) {
        List<SysOrgEntity> entityList = orgParentEntityMap.get(parentId);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        List<T> orgList = new ArrayList<>();
        entityList.stream().forEach(m -> {
            try {
                SysOrgQueryBaseVO t = clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(m, t);
                t.setChildren(getChildren(m.getOrgId(), orgParentEntityMap, clazz));
                orgList.add((T) t);
            } catch (Exception e) {
            }
        });
        return orgList;
    }

    private Map<Long, List<SysOrgEntity>> queryListCommon(SysOrgQueryRequest request) {
        List<SysOrgEntity> sysMenuEntities = sysMenuEntities = baseMapper.selectList(new QueryWrapper<SysOrgEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getOrgName()), "org_name", request.getOrgName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByAsc("sort"));
        ;
        Map<Long, List<SysOrgEntity>> orgParentEntityMap = sysMenuEntities.stream().collect(Collectors.groupingBy(SysOrgEntity::getParentId));
        return orgParentEntityMap;
    }

}
