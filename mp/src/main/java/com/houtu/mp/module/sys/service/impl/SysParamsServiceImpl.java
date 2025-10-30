package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.module.sys.dao.SysParamsDao;
import com.houtu.mp.module.sys.entity.SysParamsEntity;
import com.houtu.mp.module.sys.request.SysParamsAddRequest;
import com.houtu.mp.module.sys.request.SysParamsQueryRequest;
import com.houtu.mp.module.sys.request.SysParamsUpdateRequest;
import com.houtu.mp.module.sys.service.SysParamsService;
import com.houtu.mp.module.sys.vo.SysParamsQueryVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.web.model.ResponseData;
import com.houtu.web.model.vo.PageDataVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * sys_params 服务实现类
 * </p>
 *
 * @author jonlu
 * @since 2024-09-06
 */
@Service
public class SysParamsServiceImpl extends ServiceImpl<SysParamsDao, SysParamsEntity> implements SysParamsService {

    @Override
    public ResponseData<PageDataVO<SysParamsQueryVO>> pageQuery(SysParamsQueryRequest request) {
        Page<SysParamsEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        page = baseMapper.selectPage(page, new QueryWrapper<SysParamsEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getParamCode()), "param_code", request.getParamCode())
                .likeRight(request != null && StringUtils.isNotBlank(request.getParamName()), "param_name", request.getParamName())
                .eq(request != null && request.getDataType() != null, "data_type", request.getDataType())
                .orderByAsc("param_code"));
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            return ResponseData.success(PageDataVO.build(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords().stream().map(p -> {
                SysParamsQueryVO vo = new SysParamsQueryVO();
                BeanUtils.copyProperties(p, vo);
                return vo;
            }).toList()));
        }
        return ResponseData.success(PageDataVO.empty());
    }

    @Transactional
    @Override
    public ResponseData save(SysParamsAddRequest request) {
        if (baseMapper.exists(new QueryWrapper<SysParamsEntity>()
                .eq("param_code", request.getParamCode())
                .eq("deleted", 0))) {
            return ResponseData.fail(ErrorCode.build(42, LocaleContextHolder.getLocale()));
        }
        SysParamsEntity sysParamsEntity = new SysParamsEntity();
        BeanUtils.copyProperties(request, sysParamsEntity);
        sysParamsEntity.setCreateBy(SessionContext.getSessionUserId());
        sysParamsEntity.setCreateTime(LocalDateTime.now());
        int num = baseMapper.insert(sysParamsEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData update(SysParamsUpdateRequest request) {
        SysParamsEntity sysParamsEntity = new SysParamsEntity();
        BeanUtils.copyProperties(request, sysParamsEntity);
        sysParamsEntity.setUpdateBy(SessionContext.getSessionUserId());
        sysParamsEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(sysParamsEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData delete(List<Long> paramIds) {
        SysParamsEntity sysParamsEntity = new SysParamsEntity();
        sysParamsEntity.setDeleted(1);
        sysParamsEntity.setUpdateBy(SessionContext.getSessionUserId());
        sysParamsEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.update(sysParamsEntity, new QueryWrapper<SysParamsEntity>().in("param_id", paramIds));
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }
}
