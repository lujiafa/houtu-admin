package com.xx.mp.module.base.service.impl;

import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.base.mfa.MFAProcessor;
import com.xx.mp.module.base.mfa.MFASelector;
import com.xx.mp.module.base.request.MFASendRequest;
import com.xx.mp.module.base.request.MFAVerifyRequest;
import com.xx.mp.module.base.request.UserInfoUpdateRequest;
import com.xx.mp.module.base.request.UserUpdateMyselfPasswordRequest;
import com.xx.mp.module.base.service.UserService;
import com.xx.mp.module.base.vo.UserInfoVO;
import com.xx.mp.module.sys.dao.SysUserDao;
import com.xx.mp.module.sys.dao.SysUserOrgDao;
import com.xx.mp.module.sys.dao.SysUserPostDao;
import com.xx.mp.module.sys.dao.SysUserRoleDao;
import com.xx.mp.module.sys.entity.SysUserEntity;
import com.xx.mp.module.sys.service.SysUserService;
import com.xx.mp.module.sys.service.impl.SysUserServiceImpl;
import com.xx.mp.support.SessionContext;
import com.xx.mp.util.OTPUtils;
import io.github.lujiafa.houtu.util.common.BeanUtils;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.annotation.Resource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    @Resource
    private SysUserOrgDao sysUserOrgDao;
    @Resource
    private SysUserRoleDao sysUserRoleDao;
    @Resource
    private SysUserPostDao sysUserPostDao;

    @Override
    public UserInfoVO info() {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        Long sessionUserId = sessionUser.getUserId();
        SysUserEntity userEntity = sysLoginLogService.findById(sessionUserId);
        UserInfoVO userInfoVO = BeanUtils.copyProperties(userEntity, UserInfoVO.class);
        List<String> orgNames = sysUserOrgDao.selectUserOrgByUserId(sessionUserId).stream().map(map -> map.get("orgName")).toList();
        userInfoVO.setOrgNames(orgNames);
        List<String> postNames = sysUserPostDao.selectUserPostByUserId(sessionUserId).stream().map(map -> map.get("postName")).toList();
        userInfoVO.setOrgPosts(postNames);
        List<String> roleNames = sysUserRoleDao.selectUserRoleByUserId(sessionUserId).stream().map(map -> map.get("roleName")).toList();
        userInfoVO.setRoleNames(roleNames);
        return userInfoVO;
    }

    @Transactional
    @Override
    public ResponseData updateUserInfo(UserInfoUpdateRequest request) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        Long sessionUserId = sessionUser.getUserId();
        SysUserEntity updateEntity = new SysUserEntity();
        updateEntity.setUserId(sessionUserId);
        updateEntity.setNickName(request.getNickName());
        updateEntity.setPhone(request.getPhone());
        updateEntity.setEmail(request.getEmail());
        updateEntity.setUpdateBy(sessionUserId);
        updateEntity.setUpdateTime(LocalDateTime.now());
        userDao.updateById(updateEntity);
        sysLoginLogService.clearCache(sessionUserId);
        return ResponseData.success();
    }

    @Transactional
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
        sysLoginLogService.clearCache(sessionUserId);
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

    @Transactional
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
