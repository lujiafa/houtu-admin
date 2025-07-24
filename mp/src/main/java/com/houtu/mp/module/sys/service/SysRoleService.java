package com.houtu.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.entity.SysRoleEntity;
import com.houtu.mp.module.sys.request.SysRoleAddRequest;
import com.houtu.mp.module.sys.request.SysRoleQueryRequest;
import com.houtu.mp.module.sys.request.SysRoleUpdateRequest;
import com.houtu.web.model.vo.PageDataVO;
import com.houtu.mp.module.sys.vo.SysRoleQueryUserOperableVO;
import com.houtu.mp.module.sys.vo.SysRoleQueryVO;
import com.houtu.core.web.ResponseData;

import java.util.List;

/**
 * <p>
 * sys_role 服务类
 * </p>
 *
 * @author houtu
 * @since 2024-06-24
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    /**
     * 查询用户可操作的角色列表
     * @return List<SysRoleQueryBaseVO>
     */
    List<SysRoleQueryUserOperableVO> queryUserOperableList();

    ResponseData<PageDataVO<SysRoleQueryVO>> pageQuery(SysRoleQueryRequest request);

    ResponseData save(SysRoleAddRequest request);

    ResponseData update(SysRoleUpdateRequest request);

    ResponseData delete(List<Long> roleIds);

}
