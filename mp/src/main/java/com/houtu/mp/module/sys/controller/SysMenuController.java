package com.houtu.mp.module.sys.controller;

import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.module.sys.request.SysMenuAddRequest;
import com.houtu.mp.module.sys.request.SysMenuQueryRequest;
import com.houtu.mp.module.sys.request.SysMenuUpdateRequest;
import com.houtu.mp.module.sys.service.SysMenuService;
import com.houtu.mp.module.sys.vo.SysMenuQueryVO;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import com.houtu.web.model.response.ResponseData;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/sys/menu")
public class SysMenuController {

    @Resource
    private SysMenuService menuService;

    @PreAuthorize("hasAuthority('system:menu:query')")
    @GetMapping("/query")
    public ResponseData<List<SysMenuQueryVO>> query(SysMenuQueryRequest menuQueryRequest) {
        return ResponseData.success(menuService.queryList(menuQueryRequest));
    }

    @OperateLog(moduleType = ModuleType.MENU, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping("/add")
    public ResponseData add(@Validated SysMenuAddRequest request) {
        if (request.getMenuType() == 1 || request.getMenuType() == 2) {
            if (request.getIconType() == null) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("iconType can't be null").toArray()));
            }
            if (request.getIcon() == null || request.getIcon().trim().length() == 0) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("icon can't be null").toArray()));
            }
        }
        if (request.getMenuType() == 2) {
            if (request.getPathType() == null) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("pathType can't be null").toArray()));
            }
            if (request.getPath() == null || request.getPath().trim().length() == 0) {
                return ResponseData.fail(ErrorCode.build(30, Stream.of("path can't be empty").toArray()));
            }
        }
        return menuService.save(request);
    }

    @OperateLog(moduleType = ModuleType.MENU, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:menu:update')")
    @PutMapping("/update")
    public ResponseData update(@Validated SysMenuUpdateRequest menuUpdateRequest) {
        return menuService.update(menuUpdateRequest);
    }

    @OperateLog(moduleType = ModuleType.MENU, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(HttpServletRequest httpServletRequest, @RequestParam("menuId") @Validated @NotNull(message = "菜单ID不能为空") Long menuId) {
        boolean flag = menuService.removeById(menuId);
        if (flag) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, httpServletRequest.getLocale()));
    }

}
