package com.houtu.mp.module.sys.controller;

import com.houtu.mp.module.sys.request.SysOperateQueryRequest;
import com.houtu.mp.module.sys.service.SysOperateLogService;
import com.houtu.mp.module.sys.vo.SysOperateLogQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sys/logOperate")
public class SysOperateLogController {

    @Resource
    private SysOperateLogService sysOperateLogService;

    @PreAuthorize("hasAuthority('system:log:opt:query')")
    @GetMapping("/query")
    public ResponseData<PageDataVO<SysOperateLogQueryVO>> query(SysOperateQueryRequest request) {
        return sysOperateLogService.pageQuery(request);
    }

}
