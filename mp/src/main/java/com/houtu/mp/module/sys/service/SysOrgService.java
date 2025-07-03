package com.houtu.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.request.SysOrgAuthorizeRequest;
import com.houtu.mp.module.sys.entity.SysOrgEntity;
import com.houtu.mp.module.sys.request.SysOrgAddRequest;
import com.houtu.mp.module.sys.request.SysOrgQueryRequest;
import com.houtu.mp.module.sys.request.SysOrgUpdateRequest;
import com.houtu.mp.module.sys.vo.SysOrgQueryBaseVO;
import com.houtu.mp.module.sys.vo.SysOrgQueryVO;
import com.houtu.web.model.response.ResponseData;

import java.util.List;

/**
 * <p>
 * sys_org 服务类
 * </p>
 *
 * @author houtu
 * @since 2024-06-24
 */
public interface SysOrgService extends IService<SysOrgEntity> {

    List<SysOrgQueryBaseVO> queryBaseList(SysOrgQueryRequest request);

    List<SysOrgQueryVO> queryList(SysOrgQueryRequest request);

    ResponseData save(SysOrgAddRequest request);

    ResponseData update(SysOrgUpdateRequest request);

    ResponseData authorize(SysOrgAuthorizeRequest request);

    ResponseData delete(Long orgId);

}
