package com.houtu.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.entity.SysDictEntity;
import com.houtu.mp.module.sys.request.SysDictAddRequest;
import com.houtu.mp.module.sys.request.SysDictQueryRequest;
import com.houtu.mp.module.sys.request.SysDictUpdateRequest;
import com.houtu.mp.module.sys.vo.SysDictQueryVO;
import com.houtu.mp.module.sys.vo.SysDictSimpleVO;
import com.houtu.core.web.ResponseData;
import com.houtu.web.model.vo.PageDataVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sys_dict 服务类
 * </p>
 *
 * @author jonlu
 * @since 2024-09-06
 */
public interface SysDictService extends IService<SysDictEntity> {

    ResponseData<PageDataVO<SysDictQueryVO>> pageQuery(SysDictQueryRequest request);

    ResponseData save(SysDictAddRequest request);

    ResponseData update(SysDictUpdateRequest request);

    ResponseData delete(List<Long> dictIds);

    /**
     * 根据字典类型编码查询字典类型数据
     * @param typeCode 字典类型编码
     * @return 字典类型数据
     */
    ResponseData<SysDictSimpleVO> findByTypeCode(String typeCode);

    /**
     * 根据字典类型编码查询字典项Map
     * @param typeCode 字典类型编码
     * @return 字典项Map
     */
    Map<String, Object> findDictItemMapByTypeCode(String typeCode);

}
