package com.xx.mp.module.sys.controller;

import com.xx.mp.aspect.OperateLog;
import com.xx.mp.config.security.SecuritySupport;
import com.xx.mp.module.sys.request.SysRoleAddRequest;
import com.xx.mp.module.sys.request.SysRoleDeleteRequest;
import com.xx.mp.module.sys.request.SysRoleQueryRequest;
import com.xx.mp.module.sys.request.SysRoleUpdateRequest;
import com.xx.mp.module.sys.service.SysMenuService;
import com.xx.mp.module.sys.service.SysRoleService;
import com.xx.mp.module.sys.vo.SysMenuQueryBaseVO;
import com.xx.mp.module.sys.vo.SysRoleQueryVO;
import com.xx.mp.support.type.ModuleType;
import com.xx.mp.support.type.OperateType;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/hasAdm")
    public ResponseData<Boolean> hasAdm(@RequestParam(required = false) String perms) {
        return ResponseData.success(SecuritySupport.hasAdmin(perms));
    }

    @PreAuthorize("hasAuthority('system:role:add') || hasAuthority('system:role:update')")
    @GetMapping("/menuList")
    public ResponseData<List<SysMenuQueryBaseVO>> menuList() {
        return ResponseData.success(menuService.queryUserObtainedList());
    }

    @OperateLog(moduleType = ModuleType.ROLE, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping("/add")
    public ResponseData add(@Validated SysRoleAddRequest request) {
        return roleService.save(request);
    }

    @OperateLog(moduleType = ModuleType.ROLE, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:role:update')")
    @PutMapping("/update")
    public ResponseData update(@Validated SysRoleUpdateRequest request) {
        return roleService.update(request);
    }
    @OperateLog(moduleType = ModuleType.ROLE, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@Validated SysRoleDeleteRequest request) {
        return roleService.delete(request.getRoleIds());
    }

}
