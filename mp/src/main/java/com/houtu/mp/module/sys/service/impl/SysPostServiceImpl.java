package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.mp.module.sys.service.SysPostService;
import com.houtu.mp.support.SessionContext;
import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.module.sys.dao.SysPostDao;
import com.houtu.mp.module.sys.dao.SysPostRoleDao;
import com.houtu.mp.module.sys.entity.SysPostEntity;
import com.houtu.mp.module.sys.entity.SysPostRoleEntity;
import com.houtu.mp.module.sys.request.SysPostAddRequest;
import com.houtu.mp.module.sys.request.SysPostAuthorizeRequest;
import com.houtu.mp.module.sys.request.SysPostQueryRequest;
import com.houtu.mp.module.sys.request.SysPostUpdateRequest;
import com.houtu.web.model.vo.PageDataVO;
import com.houtu.mp.module.sys.vo.SysPostQueryBaseVO;
import com.houtu.mp.module.sys.vo.SysPostQueryVO;
import com.houtu.web.model.ResponseData;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * sys_post 服务实现类
 * </p>
 *
 * @author houtu
 * @since 2024-06-24
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostDao, SysPostEntity> implements SysPostService {

    @Resource
    private SysPostRoleDao postRoleDao;

    @Override
    public List<SysPostQueryBaseVO> queryBaseList(SysPostQueryRequest request) {
        List<SysPostEntity> list = baseMapper.selectList(new QueryWrapper<SysPostEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getPostCode()), "post_code", request.getPostCode())
                .likeRight(request != null && StringUtils.isNotBlank(request.getPostName()), "post_name", request.getPostName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByAsc("sort")
                .last("limit 1000"));
        return list.stream().map(p -> {
            SysPostQueryBaseVO vo = new SysPostQueryBaseVO();
            BeanUtils.copyProperties(p, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ResponseData<PageDataVO<SysPostQueryVO>> pageQuery(SysPostQueryRequest request) {
        Page<SysPostEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        page = baseMapper.selectPage(page, new QueryWrapper<SysPostEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getPostCode()), "post_code", request.getPostCode())
                .likeRight(request != null && StringUtils.isNotBlank(request.getPostName()), "post_name", request.getPostName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByAsc("sort"));
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            return ResponseData.success(PageDataVO.build(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords().stream().map(p -> {
                SysPostQueryVO vo = new SysPostQueryVO();
                BeanUtils.copyProperties(p, vo);
                return vo;
            }).collect(Collectors.toList())));
        }
        return ResponseData.success(PageDataVO.empty());
    }

    @Transactional
    @Override
    public ResponseData save(SysPostAddRequest request) {
        SysPostEntity sysPostEntity = new SysPostEntity();
        BeanUtils.copyProperties(request, sysPostEntity);
        sysPostEntity.setCreateBy(SessionContext.getSessionUserId());
        sysPostEntity.setCreateTime(LocalDateTime.now());
        int num = baseMapper.insert(sysPostEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Transactional
    @Override
    public ResponseData update(SysPostUpdateRequest request) {
        SysPostEntity postEntity = new SysPostEntity();
        BeanUtils.copyProperties(request, postEntity);
        postEntity.setUpdateBy(SessionContext.getSessionUserId());
        postEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(postEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Transactional
    @Override
    public ResponseData authorize(SysPostAuthorizeRequest request) {
        List<Long> reqRoleIds = request.getRoleIds() == null ? Collections.emptyList() : request.getRoleIds();
        List<Long> existsRoleIds = postRoleDao.selectList(new QueryWrapper<SysPostRoleEntity>().eq("post_id", request.getPostId())).stream().map(p -> p.getRoleId()).collect(Collectors.toList());
        List<Long> addRoleIds = reqRoleIds.stream().filter(o -> !existsRoleIds.contains(o)).collect(Collectors.toList());
        List<Long> delRoleIds = existsRoleIds.stream().filter(o -> !reqRoleIds.contains(o)).collect(Collectors.toList());
        if (addRoleIds.size() > 0) {
            postRoleDao.insert(addRoleIds.stream().map(o -> {
                SysPostRoleEntity postRoleEntity = new SysPostRoleEntity();
                postRoleEntity.setRoleId(o);
                postRoleEntity.setPostId(request.getPostId());
                return postRoleEntity;
            }).collect(Collectors.toList()));
        }
        if (delRoleIds.size() > 0) {
            postRoleDao.delete(new QueryWrapper<SysPostRoleEntity>().eq("post_id", request.getPostId()).in("role_id", delRoleIds));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData delete(List<Long> postIds) {
        SysPostEntity postEntity = new SysPostEntity();
        postEntity.setDeleted(1);
        postEntity.setUpdateBy(SessionContext.getSessionUserId());
        postEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.update(postEntity, new QueryWrapper<SysPostEntity>().in("post_id", postIds));
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }

}
