package com.houtu.mp.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 会话存储组件
 * https://docs.spring.io/spring-security/reference/6.3-SNAPSHOT/servlet/authentication/persistence.html
 */
@Component
public class BizSecurityContextRepository implements SecurityContextRepository {

    final static String SESSION_KEY = "session:%s";
    final static String REQUEST_SESSION_RENEWAL_KEY = "#session_renewal_state#";
    final static Integer SESSION_TIMEOUT = 30 * 60;

    @Resource
    private RedisTemplate redisTemplate;


    static Logger logger = LoggerFactory.getLogger(BizSecurityContextRepository.class);

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String sessionId = request.getRequestedSessionId();
        if (sessionId == null)
            return SecurityContextHolder.createEmptyContext();
        String cacheKey = String.format(SESSION_KEY, sessionId);
        SecurityContext securityContext = (SecurityContext) redisTemplate.opsForValue().get(cacheKey);
        if (securityContext == null) {
            securityContext = SecurityContextHolder.createEmptyContext();
        } else {
            if (request.getAttribute(REQUEST_SESSION_RENEWAL_KEY) == null) {
                redisTemplate.expire(cacheKey, SESSION_TIMEOUT, TimeUnit.SECONDS);
                request.setAttribute(REQUEST_SESSION_RENEWAL_KEY, true);
            }
        }
        return securityContext;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        if (context == null) return;
        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
            String cacheKey = String.format(SESSION_KEY, request.getRequestedSessionId());
            // 登录-会话存储
            redisTemplate.opsForValue().set(cacheKey, context, SESSION_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String sessionId = request.getRequestedSessionId();
        if (sessionId == null) return false;
        String cacheKey = String.format(SESSION_KEY, sessionId);
        return redisTemplate.hasKey(cacheKey);
    }

}
