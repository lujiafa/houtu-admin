package com.xx.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.mp.module.sys.entity.SysMenuEntity;
import com.xx.mp.module.sys.request.SysMenuAddRequest;
import com.xx.mp.module.sys.request.SysMenuQueryRequest;
import com.xx.mp.module.sys.request.SysMenuUpdateRequest;
import com.xx.mp.module.sys.vo.SysMenuQueryBaseVO;
import com.xx.mp.module.sys.vo.SysMenuQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;

import java.util.List;

/**
 * <p>
 * sys_menu 服务类
 * </p>
 *
 * @author houtu
 * @since 2024-06-21
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    /**
     * 查询用户已获得的菜单列表，超管拥有所有，普通用户根据授权信息获取
     * @return 列表
     */
    List<SysMenuQueryBaseVO> queryUserObtainedList();

    /**
     * 分页查询列表
     * @param request 参数
     * @return 列表
     */
    List<SysMenuQueryVO> queryList(SysMenuQueryRequest request);


    /**
     * 根据角色查询菜单列表
     * @param roleIds 角色参数【不能为空】
     * @param status 状态，可省略
     * @return 列表
     */
    List<SysMenuQueryBaseVO> selectBaseListByRoles(List<Long> roleIds, Integer status);

    ResponseData save(SysMenuAddRequest request);

    ResponseData update(SysMenuUpdateRequest request);

    ResponseData delete(Long menuId);

}
