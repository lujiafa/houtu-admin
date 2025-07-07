package com.houtu.mp.module.sys.controller;

import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.config.security.SecuritySupport;
import com.houtu.mp.module.sys.request.*;
import com.houtu.mp.module.sys.service.SysMenuService;
import com.houtu.mp.module.sys.service.SysRoleService;
import com.houtu.mp.module.sys.vo.SysMenuQueryBaseVO;
import com.houtu.mp.module.sys.vo.SysRoleQueryVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import com.houtu.web.model.response.ResponseData;
import com.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService roleService;
    @Resource
    private SysMenuService menuService;

    @PreAuthorize("hasAuthority('system:role:query')")
    @GetMapping("/query")
    public ResponseData<PageDataVO<SysRoleQueryVO>> query(SysRoleQueryRequest request) {
        return roleService.pageQuery(request);
    }

    @PreAuthorize("hasAuthority('system:role:add') || hasAuthority('system:role:update')")
    @GetMapping("/menuList")
    public ResponseData<List<SysMenuQueryBaseVO>> menuList() {
        return ResponseData.success(menuService.queryUserObtainedList());
    }

    @OperateLog(moduleType = ModuleType.ROLE, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping("/add")
    public ResponseData add(HttpServletRequest httpServletRequest, @Validated SysRoleAddRequest request) {
        boolean paramHasAdmin = SecuritySupport.hasAdmin(request.getRolePerms());
        if (!SessionContext.isAdmin() && paramHasAdmin) {
            return ResponseData.fail(ErrorCode.build(4, httpServletRequest.getLocale(), Stream.of("role perms with reserved").toArray()));
        }
        if (paramHasAdmin && request.getMenuIds() != null && request.getMenuIds().size() > 1) {
            return ResponseData.fail(ErrorCode.build(4, httpServletRequest.getLocale(), Stream.of("role admin permission do not use menuIds").toArray()));
        }
        return roleService.save(request);
    }

    @OperateLog(moduleType = ModuleType.ROLE, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:role:update')")
    @PutMapping("/update")
    public ResponseData update(HttpServletRequest httpServletRequest, @Validated SysRoleUpdateRequest request) {
        boolean paramHasAdmin = SecuritySupport.hasAdmin(request.getRolePerms());
        if (!SessionContext.isAdmin() && paramHasAdmin) {
            return ResponseData.fail(ErrorCode.build(4, httpServletRequest.getLocale(), Stream.of("role perms with reserved").toArray()));
        }
        if (paramHasAdmin && request.getMenuIds() != null && request.getMenuIds().size() > 1) {
            return ResponseData.fail(ErrorCode.build(4, httpServletRequest.getLocale(), Stream.of("role admin permission do not use menuIds").toArray()));
        }
        return roleService.update(request);
    }
    @OperateLog(moduleType = ModuleType.ROLE, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@Validated SysRoleDeleteRequest request) {
        return roleService.delete(request.getRoleIds());
    }

}
