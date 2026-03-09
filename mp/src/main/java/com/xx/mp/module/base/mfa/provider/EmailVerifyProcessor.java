package com.xx.mp.module.base.mfa.provider;

import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.base.mfa.MFAProcessor;
import com.xx.mp.module.sys.entity.SysUserEntity;
import com.xx.mp.support.SessionContext;
import io.github.lujiafa.houtu.util.web.WebUtils;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.stream.Stream;

/**
 * 邮件安全验证码
 */
public class EmailVerifyProcessor implements MFAProcessor {

    final static String CODE_ATTR_NAME = "emailCode";

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
        SessionContext.update(req);
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
