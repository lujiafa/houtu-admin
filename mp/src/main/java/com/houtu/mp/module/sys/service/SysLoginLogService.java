package com.houtu.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.entity.SysLoginLogEntity;
import com.houtu.mp.module.sys.request.SysLoginQueryRequest;
import com.houtu.mp.module.sys.vo.SysLoginLogQueryVO;
import com.houtu.core.web.ResponseData;
import com.houtu.web.model.vo.PageDataVO;

/**
 * <p>
 * sys_login_log 服务类
 * </p>
 *
 * @author jonlu
 * @since 2024-07-24
 */
public interface SysLoginLogService extends IService<SysLoginLogEntity> {

    ResponseData<PageDataVO<SysLoginLogQueryVO>> pageQuery(SysLoginQueryRequest request);

}
