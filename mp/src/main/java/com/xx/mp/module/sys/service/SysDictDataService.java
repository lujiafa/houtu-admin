package com.xx.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.mp.module.sys.entity.SysDictDataEntity;
import com.xx.mp.module.sys.request.SysDictDataAddRequest;
import com.xx.mp.module.sys.request.SysDictDataQueryRequest;
import com.xx.mp.module.sys.request.SysDictDataUpdateRequest;
import com.xx.mp.module.sys.vo.SysDictDataQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;

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
