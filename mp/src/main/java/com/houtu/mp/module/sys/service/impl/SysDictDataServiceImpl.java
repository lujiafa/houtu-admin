package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.module.sys.dao.SysDictDao;
import com.houtu.mp.module.sys.dao.SysDictDataDao;
import com.houtu.mp.module.sys.entity.SysDictDataEntity;
import com.houtu.mp.module.sys.request.SysDictDataAddRequest;
import com.houtu.mp.module.sys.request.SysDictDataQueryRequest;
import com.houtu.mp.module.sys.request.SysDictDataUpdateRequest;
import com.houtu.mp.module.sys.service.SysDictDataService;
import com.houtu.mp.module.sys.vo.SysDictDataQueryVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * sys_dict_data 服务实现类
 * </p>
 *
 * @author jonlu
 * @since 2024-09-06
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataDao, SysDictDataEntity> implements SysDictDataService {

    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public ResponseData<List<SysDictDataQueryVO>> query(SysDictDataQueryRequest request) {
        List<SysDictDataEntity> entityList =  baseMapper.selectList(new QueryWrapper<SysDictDataEntity>()
                .eq("deleted", 0)
                .eq("dict_id", request.getDictId())
                .likeRight(request != null && StringUtils.isNotBlank(request.getDictDataName()), "dict_data_name", request.getDictDataName())
                .eq(request != null && request.getStatus() != null, "status", request.getStatus())
                .orderByAsc("sort"));
        if (!entityList.isEmpty()) {
            return ResponseData.success(entityList.stream().map(p -> {
                SysDictDataQueryVO vo = new SysDictDataQueryVO();
                BeanUtils.copyProperties(p, vo);
                return vo;
            }).toList());
        }
        return ResponseData.success(Collections.emptyList());
    }

    @Override
    public ResponseData save(SysDictDataAddRequest request) {
        if (baseMapper.exists(new QueryWrapper<SysDictDataEntity>()
                .eq("dict_id", request.getDictId())
                .eq("dict_data_name", request.getDictDataName())
                .eq("deleted", 0))) {
            return ResponseData.fail(ErrorCode.build(42, LocaleContextHolder.getLocale()));
        }
        SysDictDataEntity sysDictDataEntity = new SysDictDataEntity();
        BeanUtils.copyProperties(request, sysDictDataEntity);
        sysDictDataEntity.setCreateBy(SessionContext.getSessionUserId());
        sysDictDataEntity.setCreateTime(LocalDateTime.now());
        int num = baseMapper.insert(sysDictDataEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData update(SysDictDataUpdateRequest request) {
        SysDictDataEntity sysDictDataEntity = new SysDictDataEntity();
        BeanUtils.copyProperties(request, sysDictDataEntity);
        sysDictDataEntity.setUpdateBy(SessionContext.getSessionUserId());
        sysDictDataEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(sysDictDataEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData delete(List<Long> dictDataIds) {
        SysDictDataEntity sysDictDataEntity = new SysDictDataEntity();
        sysDictDataEntity.setDeleted(1);
        sysDictDataEntity.setUpdateBy(SessionContext.getSessionUserId());
        sysDictDataEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.update(sysDictDataEntity, new QueryWrapper<SysDictDataEntity>().in("dict_data_id", dictDataIds));
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }
}
