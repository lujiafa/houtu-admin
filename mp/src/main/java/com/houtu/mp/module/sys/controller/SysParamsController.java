package com.houtu.mp.module.sys.controller;

import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.module.sys.request.*;
import com.houtu.mp.module.sys.service.SysParamsService;
import com.houtu.mp.module.sys.vo.SysParamsQueryVO;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys/params")
public class SysParamsController {

    @Resource
    private SysParamsService paramsService;

    @PreAuthorize("hasAuthority('system:params:query')")
    @GetMapping("/query")
    public ResponseData<PageDataVO<SysParamsQueryVO>> query(SysParamsQueryRequest request) {
        return paramsService.pageQuery(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:params:add')")
    @PostMapping("/add")
    public ResponseData add(@Validated SysParamsAddRequest request) {
        return paramsService.save(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:params:update')")
    @PutMapping("/update")
    public ResponseData update(@Validated SysParamsUpdateRequest request) {
        return paramsService.update(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:params:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@Validated SysParamsDeleteRequest request) {
        return paramsService.delete(request.getParamIds());
    }

}
