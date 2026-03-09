package com.xx.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.mp.module.sys.entity.SysOperateLogEntity;
import com.xx.mp.module.sys.request.SysOperateQueryRequest;
import com.xx.mp.module.sys.vo.SysOperateLogQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;

/**
 * <p>
 * sys_operate_log 服务类
 * </p>
 *
 * @author jonlu
 * @since 2024-07-24
 */
public interface SysOperateLogService extends IService<SysOperateLogEntity> {

    ResponseData<PageDataVO<SysOperateLogQueryVO>> pageQuery(SysOperateQueryRequest request);

}
