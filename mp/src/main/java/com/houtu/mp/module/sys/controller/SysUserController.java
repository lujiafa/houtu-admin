package com.houtu.mp.module.sys.controller;

import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.mp.module.base.vo.UserInfoVO;
import com.houtu.mp.module.sys.entity.SysUserEntity;
import com.houtu.mp.module.sys.request.*;
import com.houtu.mp.module.sys.service.SysOrgService;
import com.houtu.mp.module.sys.service.SysPostService;
import com.houtu.mp.module.sys.service.SysRoleService;
import com.houtu.mp.module.sys.service.SysUserService;
import com.houtu.mp.module.sys.vo.*;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import com.houtu.util.common.BeanUtils;
import com.houtu.web.model.response.ResponseData;
import com.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/user")
public class SysUserController {

    @Resource
    private SysUserService userService;
    @Resource
    private SysPostService postService;
    @Resource
    private SysOrgService orgService;
    @Resource
    private SysRoleService roleService;

    @PreAuthorize("hasAuthority('system:user:query')")
    @GetMapping("/query")
    public ResponseData<PageDataVO<SysUserQueryVO>> query(SysUserQueryRequest request) {
        return userService.pageQuery(request);
    }

    @PreAuthorize("hasAuthority('system:user:list') || hasAuthority('system:user:query')")
    @GetMapping("/orgList")
    public ResponseData<List<SysOrgQueryBaseVO>> query(SysOrgQueryRequest orgQueryRequest) {
        return ResponseData.success(orgService.queryBaseList(orgQueryRequest));
    }

    /**
     * 获取所有有效岗位列表
     * @return List<SysPostQueryBaseVO> 岗位列表
     */
    @PreAuthorize("hasAuthority('system:user:add') || hasAuthority('system:user:update')")
    @GetMapping("/postList")
    public ResponseData<List<SysPostQueryBaseVO>> postList() {
        SysPostQueryRequest request = new SysPostQueryRequest();
        return ResponseData.success(postService.queryBaseList(request));
    }

    /**
     * 获取所有有效角色列表
     * @return List<SysRoleQueryBaseVO> 角色列表
     */
    @PreAuthorize("hasAuthority('system:user:authorize')")
    @GetMapping("/roleList")
    public ResponseData<List<SysRoleQueryBaseVO>> roleList() {
        SysRoleQueryRequest request = new SysRoleQueryRequest();
        List<SysRoleQueryBaseVO> roleQueryList = roleService.queryBaseList(request);
        // 非超级管理员用户时，返回列表中过滤掉超级管理员角色
        if (!SessionContext.isAdmin()) {
            roleQueryList = roleQueryList.stream().filter(role -> !role.isAdmin()).toList();
        }
        return ResponseData.success(roleQueryList);
    }

    /**
     * 判断当前用户是否是超级管理员
     * @return ResponseData true:是超级管理员，false:非超级管理员
     */
    @PreAuthorize("hasAuthority('system:user:add') || hasAuthority('system:user:update')")
    @GetMapping("/adm")
    public ResponseData<Boolean> adm() {
        return ResponseData.success(SessionContext.isAdmin());
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping("/add")
    public ResponseData<SysUserSecretVO> add(@Validated SysUserAddRequest request) {
        return userService.save(request);
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:user:update')")
    @PutMapping("/update")
    public ResponseData update(@Validated SysUserUpdateRequest request) {
        return userService.update(request);
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.AUTHORIZE)
    @PreAuthorize("hasAuthority('system:user:authorize')")
    @PutMapping("/authorize")
    public ResponseData authorize(@Validated SysUserAuthorizeRequest request) {
        return userService.authorize(request);
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.UPDATE, ignoreResponseData = true)
    @PreAuthorize("hasAuthority('system:user:secret:reset')")
    @PostMapping("/resetSecret")
    public ResponseData<SysUserSecretVO> resetSecret(@Validated SysUserResetSecretRequest request) {
        return userService.resetSecret(request.getUserId());
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@RequestParam("userId") @Validated @NotNull(message = "userId can't be null") Long userId) {
        return userService.delete(userId);
    }

}
