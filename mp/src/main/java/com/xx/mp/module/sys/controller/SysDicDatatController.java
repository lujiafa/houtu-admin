package com.xx.mp.module.sys.controller;

import com.xx.mp.aspect.OperateLog;
import com.xx.mp.module.sys.request.*;
import com.xx.mp.module.sys.service.SysDictDataService;
import com.xx.mp.module.sys.vo.SysDictDataQueryVO;
import com.xx.mp.support.type.ModuleType;
import com.xx.mp.support.type.OperateType;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/dictData")
public class SysDicDatatController {

    @Resource
    private SysDictDataService dictDataService;

    @PreAuthorize("hasAuthority('system:dict:query')")
    @GetMapping("/query")
    public ResponseData<List<SysDictDataQueryVO>> query(SysDictDataQueryRequest request) {
        return dictDataService.query(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:dict:add')")
    @PostMapping("/add")
    public ResponseData add(@Validated SysDictDataAddRequest request) {
        return dictDataService.save(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:dict:update')")
    @PutMapping("/update")
    public ResponseData update(@Validated SysDictDataUpdateRequest request) {
        return dictDataService.update(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:dict:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@Validated SysDictDataDeleteRequest request) {
        return dictDataService.delete(request.getDictDataIds());
    }

}
