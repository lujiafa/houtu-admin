package com.houtu.mp.module.base.controller;

import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.module.base.request.UserInfoUpdateRequest;
import com.houtu.mp.module.base.request.UserUpdateMyselfPasswordRequest;
import com.houtu.mp.module.base.service.UserService;
import com.houtu.mp.module.base.vo.UserInfoVO;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import com.houtu.web.model.ResponseData;
import javax.annotation.Resource;
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
