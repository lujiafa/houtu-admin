package com.houtu.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.entity.SysDictDataEntity;
import com.houtu.mp.module.sys.request.SysDictDataAddRequest;
import com.houtu.mp.module.sys.request.SysDictDataQueryRequest;
import com.houtu.mp.module.sys.request.SysDictDataUpdateRequest;
import com.houtu.mp.module.sys.vo.SysDictDataQueryVO;
import com.houtu.web.model.response.ResponseData;

import java.util.List;

/**
 * <p>
 * sys_DictData_data 服务类
 * </p>
 *
 * @author jonlu
 * @since 2024-09-06
 */
public interface SysDictDataService extends IService<SysDictDataEntity> {

    ResponseData<List<SysDictDataQueryVO>> query(SysDictDataQueryRequest request);

    ResponseData save(SysDictDataAddRequest request);

    ResponseData update(SysDictDataUpdateRequest request);

    ResponseData delete(List<Long> dictDataIds);

}
