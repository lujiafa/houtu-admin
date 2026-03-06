package com.houtu.mp.module.sys.controller;

import com.houtu.mp.module.sys.request.SysOrgAuthorizeRequest;
import com.houtu.mp.module.sys.service.SysOrgService;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.module.sys.request.SysOrgAddRequest;
import com.houtu.mp.module.sys.request.SysOrgQueryRequest;
import com.houtu.mp.module.sys.request.SysOrgUpdateRequest;
import com.houtu.mp.module.sys.vo.SysOrgQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/sys/org")
public class SysOrgController {

    @Resource
    private SysOrgService orgService;

    @PreAuthorize("hasAuthority('system:org:query')")
    @GetMapping("/query")
    public ResponseData<List<SysOrgQueryVO>> query(SysOrgQueryRequest orgQueryRequest) {
        return ResponseData.success(orgService.queryList(orgQueryRequest));
    }

    @OperateLog(moduleType = ModuleType.ORG, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:org:add')")
    @PostMapping("/add")
    public ResponseData add(@Validated SysOrgAddRequest request) {
        return orgService.save(request);
    }

    @OperateLog(moduleType = ModuleType.ORG, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:org:update')")
    @PutMapping("/update")
    public ResponseData update(HttpServletRequest httpServletRequest, @Validated SysOrgUpdateRequest request) {
        if (request.getParentId() == request.getOrgId()) {
            return ResponseData.fail(ErrorCode.build(4, httpServletRequest.getLocale(), Stream.of("orgId cannot be same as the parentId").toArray()));
        }
        return orgService.update(request);
    }

    @OperateLog(moduleType = ModuleType.ORG, operateType = OperateType.AUTHORIZE)
    @PreAuthorize("hasAuthority('system:org:authorize')")
    @PutMapping("/authorize")
    public ResponseData authorize(@Validated SysOrgAuthorizeRequest request) {
        return orgService.authorize(request);
    }

    @OperateLog(moduleType = ModuleType.ORG, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:org:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@RequestParam("orgId") @Validated @NotNull(message = "orgId can't be null") Long orgId) {
        return orgService.delete(orgId);
    }

}
