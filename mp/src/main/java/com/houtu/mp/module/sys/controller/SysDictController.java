package com.houtu.mp.module.sys.controller;

import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.module.sys.request.*;
import com.houtu.mp.module.sys.service.SysDictService;
import com.houtu.mp.module.sys.service.SysDictService;
import com.houtu.mp.module.sys.vo.DictTypeVO;
import com.houtu.mp.module.sys.vo.SysDictQueryVO;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import com.houtu.web.model.response.ResponseData;
import com.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys/dict")
public class SysDictController {

    @Resource
    private SysDictService dictService;

    @GetMapping("/find")
    public ResponseData<DictTypeVO> findByTypeCode(@RequestParam(required = false) String typeCode) {
        return dictService.findByTypeCode(typeCode);
    }

    @PreAuthorize("hasAuthority('system:dict:query')")
    @GetMapping("/query")
    public ResponseData<PageDataVO<SysDictQueryVO>> query(SysDictQueryRequest request) {
        return dictService.pageQuery(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:dict:add')")
    @PostMapping("/add")
    public ResponseData add(@Validated SysDictAddRequest request) {
        return dictService.save(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:dict:update')")
    @PutMapping("/update")
    public ResponseData update(@Validated SysDictUpdateRequest request) {
        return dictService.update(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:dict:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@Validated SysDictDeleteRequest request) {
        return dictService.delete(request.getDictIds());
    }

}
