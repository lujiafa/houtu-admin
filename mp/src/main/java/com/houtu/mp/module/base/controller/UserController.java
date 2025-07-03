package com.houtu.mp.module.base.controller;

import com.houtu.mp.module.base.request.UserUpdateMyselfPasswordRequest;
import com.houtu.mp.module.base.service.UserService;
import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import com.houtu.web.model.response.ResponseData;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.UPDATE)
    @PutMapping("/updateMyselfPassword")
    public ResponseData updateMyselfPassword(@Validated UserUpdateMyselfPasswordRequest request) {
        return userService.updateMyselfPassword(request);
    }
}
