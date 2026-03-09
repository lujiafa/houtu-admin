package com.xx.mp.module.base.controller;

import com.xx.mp.aspect.OperateLog;
import com.xx.mp.module.base.request.UserInfoUpdateRequest;
import com.xx.mp.module.base.request.UserUpdateMyselfPasswordRequest;
import com.xx.mp.module.base.service.UserService;
import com.xx.mp.module.base.vo.UserInfoVO;
import com.xx.mp.support.type.ModuleType;
import com.xx.mp.support.type.OperateType;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/info")
    public ResponseData<UserInfoVO> info() {
        UserInfoVO userInfoVO = userService.info();
        return ResponseData.success(userInfoVO);
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.UPDATE)
    @PutMapping("/updateUserInfo")
    public ResponseData updateUserInfo(@Validated UserInfoUpdateRequest request) {
        return userService.updateUserInfo(request);
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.UPDATE)
    @PutMapping("/updateMyselfPassword")
    public ResponseData updateMyselfPassword(@Validated UserUpdateMyselfPasswordRequest request) {
        return userService.updateMyselfPassword(request);
    }
}
