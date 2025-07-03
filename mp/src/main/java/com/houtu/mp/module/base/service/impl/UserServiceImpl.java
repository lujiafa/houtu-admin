package com.houtu.mp.module.base.service.impl;

import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.mp.module.base.mfa.MFAProcessor;
import com.houtu.mp.module.base.mfa.MFASelector;
import com.houtu.mp.module.base.request.MFASendRequest;
import com.houtu.mp.module.base.request.MFAVerifyRequest;
import com.houtu.mp.module.base.request.UserUpdateMyselfPasswordRequest;
import com.houtu.mp.module.base.service.UserService;
import com.houtu.mp.module.sys.dao.SysUserDao;
import com.houtu.mp.module.sys.entity.SysUserEntity;
import com.houtu.mp.module.sys.service.SysUserService;
import com.houtu.mp.module.sys.service.impl.SysUserServiceImpl;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.util.OTPUtils;
import com.houtu.web.model.response.ResponseData;
import jakarta.annotation.Resource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private SysUserDao userDao;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserServiceImpl sysLoginLogService;

    @Override
    public ResponseData updateMyselfPassword(UserUpdateMyselfPasswordRequest request) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        Long sessionUserId = sessionUser.getUserId();
        SysUserEntity sysUserEntity = userDao.selectById(sessionUserId);
        if (!passwordEncoder.matches(request.getOldPassword(), sysUserEntity.getPassword())) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale(), "old password error"));
        }
        SysUserEntity updateEntity = new SysUserEntity();
        updateEntity.setUserId(sessionUserId);
        updateEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        updateEntity.setUpdateBy(sessionUserId);
        updateEntity.setUpdateTime(LocalDateTime.now());
        userDao.updateById(updateEntity);
        sysLoginLogService.clearCache(sessionUser.getUserId());
        return ResponseData.success();
    }

    @Override
    public ResponseData sendMFA(MFASendRequest request) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        SysUserEntity sysUserEntity = sysUserService.findByUsername(sessionUser.getUsername());
        MFAProcessor processor = MFASelector.getMFAProcessor(request.getMfaType());
        return processor.push(sysUserEntity);
    }

    @Override
    public ResponseData verifyMFA(MFAVerifyRequest request) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        SysUserEntity sysUserEntity = sysUserService.findByUsername(sessionUser.getUsername());
        if (sysUserEntity == null) {
            return ResponseData.fail(ErrorCode.build(1, LocaleContextHolder.getLocale()));
        }
        MFAProcessor mfaProcessor = MFASelector.getMFAProcessor(request.getMfaType());
        return mfaProcessor.verify(sysUserEntity, request.getCode());
    }

    @Transactional
    @Override
    public ResponseData openMFA() {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        SysUserEntity updateEntity = new SysUserEntity();
        updateEntity.setUserId(sessionUser.getUserId());
        updateEntity.setEnableMFA(1);
        userDao.updateById(updateEntity);
        sysLoginLogService.clearCache(sessionUser.getUserId());
        return ResponseData.success();
    }

    @Override
    public ResponseData<String> myselfOTP() {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        SysUserEntity sysUserEntity = sysUserService.findByUsername(sessionUser.getUsername());
        if (sysUserEntity == null) {
            return ResponseData.fail(ErrorCode.build(1,LocaleContextHolder.getLocale()));
        }
        return ResponseData.success(OTPUtils.generateQRCode(sessionUser.getUsername(), sysUserEntity.getGoogleOTPKey()));
    }

    @Override
    public ResponseData<String> resetMyselfOTP() {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        SysUserEntity updateEntity = new SysUserEntity();
        updateEntity.setUserId(sessionUser.getUserId());
        String GoogleOTPKey = OTPUtils.generateSecretKey();
        updateEntity.setGoogleOTPKey(GoogleOTPKey);
        updateEntity.setUpdateBy(sessionUser.getUserId());
        updateEntity.setUpdateTime(LocalDateTime.now());
        userDao.updateById(updateEntity);
        sysLoginLogService.clearCache(sessionUser.getUserId());
        return ResponseData.success(OTPUtils.generateQRCode(sessionUser.getUsername(), GoogleOTPKey));
    }

}
