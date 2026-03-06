package com.houtu.mp.module.base.controller;

import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.houtu.mp.aspect.OperateLog;
import com.houtu.mp.config.SecurityProperties;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.mp.module.base.mfa.MFAProcessor;
import com.houtu.mp.module.base.mfa.MFASelector;
import com.houtu.mp.module.base.mfa.provider.GoogleOTPVerifyProcessor;
import com.houtu.mp.module.base.request.MFASendRequest;
import com.houtu.mp.module.base.request.MFAVerifyRequest;
import com.houtu.mp.module.base.service.LoginLogService;
import com.houtu.mp.module.base.service.UserService;
import com.houtu.mp.module.base.vo.MFAStateVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.support.type.LoginType;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;
import io.github.lujiafa.houtu.web.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/api/mfa")
public class MFAController {

    @Resource
    private SecurityProperties securityProperties;
    @Resource
    private UserService userService;
    @Resource
    private LoginLogService loginLogService;

    /**
     * 获取MFA启用状态，是否启用MFA
     * @param req
     * @param resp
     * @return
     */
    @GetMapping("/state")
    public ResponseData<MFAStateVO> state(HttpServletRequest req, HttpServletResponse resp) {
        MFAStateVO mfaStateVO = new MFAStateVO();
        mfaStateVO.setSupportMFA(securityProperties.isMfa());
        if (!securityProperties.isMfa()) {
            return ResponseData.success(mfaStateVO);
        }
        mfaStateVO.setMyselfMFA(SessionContext.getSessionUser().isEnableMFA());
        mfaStateVO.setOtp(mfaStateVO.isMyselfMFA() && MFASelector.getMfaTypes().parallelStream().anyMatch(t -> GoogleOTPVerifyProcessor.MFA_TYPE.equals(t.getMfaType())));
        return ResponseData.success(mfaStateVO);
    }

    /**
     * 暂时个人MFA中Google OTP数据
     * @return OTP 数据
     */
    @GetMapping("/showOTP")
    public ResponseData<String> showOTP() {
        if (!securityProperties.isMfa()) {
            return ResponseData.fail(ErrorCode.build(5, LocaleContextHolder.getLocale()));
        }
        return userService.myselfOTP();
    }

    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.UPDATE, ignoreResponseData = true)
    @PostMapping("/open")
    public ResponseData openMFA(HttpServletRequest req, HttpServletResponse resp) {
        if (!securityProperties.isMfa()) {
            return ResponseData.fail(ErrorCode.build(5, LocaleContextHolder.getLocale()));
        }
        ResponseData responseData = userService.openMFA();
        if (!responseData.hasSuccess())
            return responseData;
        // 更新sessionMFA启用并默认初次通过
        SimpleUser sessionUser = SessionContext.getSessionUser();
        sessionUser.setEnableMFA(true);
        sessionUser.setMfaPassed(true);
        SessionContext.update(req);
        return responseData;
    }

    /**
     * 重置个人MFA中Google OTP数据
     * @return
     */
    @OperateLog(moduleType = ModuleType.USER, operateType = OperateType.UPDATE, ignoreResponseData = true)
    @PostMapping("/resetOTP")
    public ResponseData<String> resetOTP() {
        return userService.resetMyselfOTP();
    }

    /**
     * 获取MFA类型
     * @return MFA类型数据
     */
    @GetMapping("/mfaTypes")
    public ResponseData<List<MFAProcessor.MFAType>> mfaTypes() {
        return ResponseData.success(MFASelector.getMfaTypes());
    }

    /**
     * 发送MFA验证码
     * @param request 请求参数
     * @return 发送结果
     */
    @PostMapping("/send")
    public ResponseData sendMFA(MFASendRequest request) {
        if (!securityProperties.isMfa()) {
            return ResponseData.fail(ErrorCode.build(5));
        }
        return userService.sendMFA(request);
    }

    /**
     * MFA验证验证信息
     * @param req 请求
     * @param resp 响应
     * @param request 请求参数
     * @return 验证结果
     */
    @PostMapping("/verify")
    public ResponseData verifyMFA(HttpServletRequest req, HttpServletResponse resp, @Validated MFAVerifyRequest request) {
        if (!securityProperties.isMfa()) {
            return ResponseData.fail(ErrorCode.build(5, LocaleContextHolder.getLocale()));
        }
        if (!StringUtils.isNumeric(request.getCode())) {
            return ResponseData.fail(ErrorCode.build(30, LocaleContextHolder.getLocale()));
        }
        ResponseData responseData = userService.verifyMFA(request);
        if (responseData.hasSuccess()) {
            SimpleUser sessionUser = SessionContext.getSessionUser();
            sessionUser.setMfaPassed(true);
            SessionContext.update(req);
            // 登录成功记录登录日志
            loginLogService.log(req, LoginType.LOGIN, sessionUser.getUsername());
        }
        return responseData;
    }

}
