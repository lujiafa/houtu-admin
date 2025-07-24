package com.houtu.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.entity.SysParamsEntity;
import com.houtu.mp.module.sys.request.SysParamsAddRequest;
import com.houtu.mp.module.sys.request.SysParamsQueryRequest;
import com.houtu.mp.module.sys.request.SysParamsUpdateRequest;
import com.houtu.mp.module.sys.vo.SysParamsQueryVO;
import com.houtu.core.web.ResponseData;
import com.houtu.web.model.vo.PageDataVO;

import java.util.List;

/**
 * <p>
 * sys_params 服务类
 * </p>
 *
 * @author jonlu
 * @since 2024-09-06
 */
public interface SysParamsService extends IService<SysParamsEntity> {

    ResponseData<PageDataVO<SysParamsQueryVO>> pageQuery(SysParamsQueryRequest request);

    ResponseData save(SysParamsAddRequest request);

    ResponseData update(SysParamsUpdateRequest request);

    ResponseData delete(List<Long> ParamsIds);

}
