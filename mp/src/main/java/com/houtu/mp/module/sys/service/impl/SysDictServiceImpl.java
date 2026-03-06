package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.houtu.mp.module.sys.dao.SysDictDao;
import com.houtu.mp.module.sys.dao.SysDictDataDao;
import com.houtu.mp.module.sys.entity.SysDictDataEntity;
import com.houtu.mp.module.sys.entity.SysDictEntity;
import com.houtu.mp.module.sys.request.SysDictAddRequest;
import com.houtu.mp.module.sys.request.SysDictQueryRequest;
import com.houtu.mp.module.sys.request.SysDictUpdateRequest;
import com.houtu.mp.module.sys.service.SysDictService;
import com.houtu.mp.module.sys.vo.SysDictDataSimpleVO;
import com.houtu.mp.module.sys.vo.SysDictQueryVO;
import com.houtu.mp.module.sys.vo.SysDictSimpleVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.support.type.CommonStatus;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    final static String DICT_TYPE_CODE_CACHE_KEY = "dict:type_code:list:%s";

    @Resource
    private SysDictDataDao dictDataDao;
    @Resource
    private RedisTemplate redisTemplate;

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
            }).collect(Collectors.toList())));
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

    @Override
    public ResponseData<SysDictSimpleVO> findByTypeCode(String typeCode) {
        Assert.notNull(typeCode, "typeCode can not be null");
        String cacheKey = String.format(DICT_TYPE_CODE_CACHE_KEY, typeCode);
        Object obj = redisTemplate.opsForValue().get(cacheKey);
        if (obj != null) {
            if (obj instanceof SysDictSimpleVO) {
                return ResponseData.success((SysDictSimpleVO) obj);
            }
            return ResponseData.fail(ErrorCode.build(41));
        }
        SysDictEntity sysDictEntity = baseMapper.selectOne(new QueryWrapper<SysDictEntity>()
                .eq("type_code", typeCode)
                .eq("status", CommonStatus.ENABLED.getStatus())
                .eq("deleted", 0)
                .last("limit 1"));
        if (sysDictEntity == null) {
            redisTemplate.opsForValue().set(cacheKey, StringUtils.EMPTY, 5, TimeUnit.SECONDS);
            return ResponseData.fail(ErrorCode.build(41));
        }
        SysDictSimpleVO vo = new SysDictSimpleVO();
        vo.setTypeCode(sysDictEntity.getTypeCode());
        vo.setTypeName(sysDictEntity.getTypeName());
        vo.setTypeDesc(sysDictEntity.getTypeDesc());
        List<SysDictDataEntity> dictDataEntityList = dictDataDao.selectList(new QueryWrapper<SysDictDataEntity>()
                .eq("type_code", typeCode)
                .eq("status", CommonStatus.ENABLED.getStatus())
                .eq("deleted", 0)
                .orderByAsc("sort"));
        if (dictDataEntityList != null && dictDataEntityList.size() > 0) {
            vo.setList(dictDataEntityList.stream().map(p -> {
                SysDictDataSimpleVO item = new SysDictDataSimpleVO();
                item.setDictDataName(p.getDictDataName());
                item.setDictDataValue(p.getDictDataValue());
                item.setDictDataDesc(p.getDictDataDesc());
                return item;
            }).collect(Collectors.toList()));
        }
        redisTemplate.opsForValue().set(cacheKey, vo, 15, TimeUnit.SECONDS);
        return ResponseData.success(vo);
    }

    @Override
    public Map<String, Object> findDictItemMapByTypeCode(String typeCode) {
        ResponseData<SysDictSimpleVO> responseData = findByTypeCode(typeCode);
        if (responseData.hasSuccess()) {
            return responseData.getData().getList().stream().collect(Collectors.toMap(SysDictDataSimpleVO::getDictDataValue, SysDictDataSimpleVO::getDictDataName));
        }
        return Collections.emptyMap();
    }
}
