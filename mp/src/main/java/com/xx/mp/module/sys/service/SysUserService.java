package com.xx.mp.module.sys.service;

import com.xx.mp.module.sys.entity.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.mp.module.sys.request.SysUserAddRequest;
import com.xx.mp.module.sys.request.SysUserAuthorizeRequest;
import com.xx.mp.module.sys.request.SysUserQueryRequest;
import com.xx.mp.module.sys.request.SysUserUpdateRequest;
import com.xx.mp.module.sys.vo.SysUserSecretVO;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import com.xx.mp.module.sys.vo.SysUserQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;

/**
 * <p>
 * sys_user 服务类
 * </p>
 *
 * @author houtu
 * @since 2024-06-21
 */
public interface SysUserService extends IService<SysUserEntity> {

    SysUserEntity findById(Long userId);

    SysUserEntity findByUsername(String username);

    ResponseData<PageDataVO<SysUserQueryVO>> pageQuery(SysUserQueryRequest request);

    ResponseData<SysUserSecretVO> save(SysUserAddRequest request);

    ResponseData update(SysUserUpdateRequest request);

    ResponseData<SysUserSecretVO> resetSecret(Long userId);

    ResponseData authorize(SysUserAuthorizeRequest request);

    ResponseData delete(Long userId);

}
