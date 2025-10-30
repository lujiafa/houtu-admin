package com.houtu.mp.module.base.mfa.provider;

import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.config.security.BizSecurityContextRepository;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.mp.module.base.mfa.MFAProcessor;
import com.houtu.mp.module.sys.entity.SysUserEntity;
import com.houtu.mp.support.SessionContext;
import com.houtu.util.web.WebUtils;
import com.houtu.web.model.ResponseData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.stream.Stream;

/**
 * 邮件安全验证码
 */
public class EmailVerifyProcessor implements MFAProcessor {

    final static String CODE_ATTR_NAME = "emailCode";

    @Autowired
    private BizSecurityContextRepository bizSecurityContextRepository;

    @Override
    public MFAType getMfaType() {
        return new MFAType("邮件验证码", "EMAIL", false);
    }

    @Override
    public ResponseData push(SysUserEntity sysUserEntity) {
        if (StringUtils.isBlank(sysUserEntity.getEmail())) {
            return ResponseData.fail(ErrorCode.build(14, Stream.of("请先绑定邮箱").toArray()));
        }
        String code = String.format("%4d", RandomStringUtils.randomNumeric(4));
        SimpleUser sessionUser = SessionContext.getSessionUser();
        sessionUser.getAttrs().put(CODE_ATTR_NAME, code);
        // 开始发送验证码
        // ...
        // 发送验证码完成
        ServletRequestAttributes requestAttributes = WebUtils.getServletRequestAttributes();
        HttpServletRequest req = requestAttributes.getRequest();
        HttpServletResponse resp = requestAttributes.getResponse();
        bizSecurityContextRepository.saveContext(SecurityContextHolder.getContext(), req, resp);
        return ResponseData.success();
    }

    @Override
    public ResponseData verify(SysUserEntity sysUserEntity, String code) {
        Assert.notNull(code, "验证码不能为空");
        SimpleUser sessionUser = SessionContext.getSessionUser();
        if (!StringUtils.equals(code, (CharSequence) sessionUser.getAttrs().get(CODE_ATTR_NAME))) {
            return ResponseData.fail(ErrorCode.build(17));
        }
        return ResponseData.success();
    }
}
