package com.xx.mp.module.base.mfa.provider;

import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.base.mfa.MFAProcessor;
import com.xx.mp.module.sys.entity.SysUserEntity;
import com.xx.mp.support.SessionContext;
import io.github.lujiafa.houtu.web.model.ResponseData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 企业微信安全验证码
 */
//@Component
public class WorkWeiXinVerifyProcessor implements MFAProcessor {

    final static String CACHE_KEY_PATTERN = "work:weixin:verify:code:%s";

    //@Autowired
    private RedisTemplate redisTemplate;

    @Override
    public MFAType getMfaType() {
        return new MFAType("企微验证码", "WORK_WEI_XIN", false);
    }

    @Override
    public ResponseData push(SysUserEntity sysUserEntity) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        if (StringUtils.isBlank(sysUserEntity.getPhone())) {
            return ResponseData.fail(ErrorCode.build(14, Stream.of("请先配置手机号").toArray()));
        }
        String code = RandomStringUtils.secure().nextNumeric(1000, 9999);
        String key = String.format(CACHE_KEY_PATTERN, sessionUser.getUserId());
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        // 发送企微消息开始

        // 发送企微消息完成
        return ResponseData.success();
    }

    @Override
    public ResponseData verify(SysUserEntity sysUserEntity, String code) {
        Assert.notNull(code, "验证码不能为空");
        SimpleUser sessionUser = SessionContext.getSessionUser();
        String key = String.format(CACHE_KEY_PATTERN, sessionUser.getUserId());
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null || !Objects.equals(value, code)) {
            return ResponseData.fail(ErrorCode.build(17));
        }
        return ResponseData.success();
    }
}
