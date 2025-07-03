package com.houtu.mp.module.sys.controller;

import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.module.sys.request.*;
import com.houtu.mp.module.sys.request.*;
import com.houtu.mp.module.sys.service.SysPostService;
import com.houtu.web.model.vo.PageDataVO;
import com.houtu.mp.module.sys.vo.SysPostQueryVO;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import com.houtu.web.model.response.ResponseData;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sys/post")
public class SysPostController {

    @Resource
    private SysPostService postService;

    @PreAuthorize("hasAuthority('system:post:query')")
    @GetMapping("/query")
    public ResponseData<PageDataVO<SysPostQueryVO>> query(SysPostQueryRequest request) {
        return postService.pageQuery(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.ADD)
    @PreAuthorize("hasAuthority('system:post:add')")
    @PostMapping("/add")
    public ResponseData add(@Validated SysPostAddRequest request) {
        return postService.save(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.UPDATE)
    @PreAuthorize("hasAuthority('system:post:update')")
    @PutMapping("/update")
    public ResponseData update(@Validated SysPostUpdateRequest request) {
        return postService.update(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.AUTHORIZE)
    @PreAuthorize("hasAuthority('system:post:authorize')")
    @PutMapping("/authorize")
    public ResponseData authorize(@Validated SysPostAuthorizeRequest request) {
        return postService.authorize(request);
    }

    @OperateLog(moduleType = ModuleType.POST, operateType = OperateType.DELETE)
    @PreAuthorize("hasAuthority('system:post:delete')")
    @DeleteMapping("/delete")
    public ResponseData delete(@Validated SysPostDeleteRequest request) {
        return postService.delete(request.getPostIds());
    }

}
