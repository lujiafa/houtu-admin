package com.xx.mp.module.sys.controller;

import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.xx.mp.aspect.OperateLog;
import com.xx.mp.module.sys.request.SysMenuAddRequest;
import com.xx.mp.module.sys.request.SysMenuQueryRequest;
import com.xx.mp.module.sys.request.SysMenuUpdateRequest;
import com.xx.mp.module.sys.service.SysMenuService;
import com.xx.mp.module.sys.vo.SysMenuQueryVO;
import com.xx.mp.support.type.ModuleType;
import com.xx.mp.support.type.OperateType;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
