package com.houtu.mp.module.sys.controller;

import com.houtu.mp.module.sys.service.SysLoginLogService;
import com.houtu.mp.module.sys.request.SysLoginQueryRequest;
import com.houtu.mp.module.sys.vo.SysLoginLogQueryVO;
import com.houtu.web.model.response.ResponseData;
import com.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sys/logLogin")
public class SysLoginLogController {

    @Resource
    private SysLoginLogService sysLoginLogService;

    @PreAuthorize("hasAuthority('system:log:login:query')")
    @GetMapping("/query")
    public ResponseData<PageDataVO<SysLoginLogQueryVO>> query(SysLoginQueryRequest request) {
        return sysLoginLogService.pageQuery(request);
    }

}
