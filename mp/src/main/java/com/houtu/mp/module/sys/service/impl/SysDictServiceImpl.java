package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.mp.module.sys.dao.SysDictDao;
import com.houtu.mp.module.sys.entity.SysDictEntity;
import com.houtu.mp.module.sys.request.SysDictAddRequest;
import com.houtu.mp.module.sys.request.SysDictQueryRequest;
import com.houtu.mp.module.sys.request.SysDictUpdateRequest;
import com.houtu.mp.module.sys.service.SysDictService;
import com.houtu.mp.module.sys.vo.SysDictQueryVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.core.exception.ErrorCode;
import com.houtu.web.model.response.ResponseData;
import com.houtu.web.model.vo.PageDataVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * sys_dict 服务实现类
 * </p>
 *
 * @author jonlu
 * @since 2024-09-06
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public ResponseData<PageDataVO<SysDictQueryVO>> pageQuery(SysDictQueryRequest request) {
        Page<SysDictEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        page = baseMapper.selectPage(page, new QueryWrapper<SysDictEntity>()
                .eq("deleted", 0)
                .likeRight(request != null && StringUtils.isNotBlank(request.getTypeCode()), "type_code", request.getTypeCode())
                .likeRight(request != null && StringUtils.isNotBlank(request.getTypeName()), "type_name", request.getTypeName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByDesc("create_time"));
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            return ResponseData.success(PageDataVO.build(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords().stream().map(p -> {
                SysDictQueryVO vo = new SysDictQueryVO();
                BeanUtils.copyProperties(p, vo);
                return vo;
            }).toList()));
        }
        return ResponseData.success(PageDataVO.empty());
    }

    @Override
    public ResponseData save(SysDictAddRequest request) {
        if (baseMapper.exists(new QueryWrapper<SysDictEntity>()
                .eq("type_code", request.getTypeCode())
                .eq("deleted", 0))) {
            return ResponseData.fail(ErrorCode.build(42, LocaleContextHolder.getLocale()));
        }
        SysDictEntity sysDictEntity = new SysDictEntity();
        BeanUtils.copyProperties(request, sysDictEntity);
        sysDictEntity.setCreateBy(SessionContext.getSessionUserId());
        sysDictEntity.setCreateTime(LocalDateTime.now());
        int num = baseMapper.insert(sysDictEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData update(SysDictUpdateRequest request) {
        SysDictEntity sysDictEntity = new SysDictEntity();
        BeanUtils.copyProperties(request, sysDictEntity);
        sysDictEntity.setUpdateBy(SessionContext.getSessionUserId());
        sysDictEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(sysDictEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData delete(List<Long> dictIds) {
        SysDictEntity sysDictEntity = new SysDictEntity();
        sysDictEntity.setDeleted(1);
        sysDictEntity.setUpdateBy(SessionContext.getSessionUserId());
        sysDictEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.update(sysDictEntity, new QueryWrapper<SysDictEntity>().in("dict_id", dictIds));
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }
}
