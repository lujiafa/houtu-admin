//package com.xx.mp.config.security;
//
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.context.DeferredSecurityContext;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.web.context.HttpRequestResponseHolder;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * 会话存储组件
// * https://docs.spring.io/spring-security/reference/6.3-SNAPSHOT/servlet/authentication/persistence.html
// */
//@Component
//public class BizSecurityContextRepository implements SecurityContextRepository {
//
//    final static String SESSION_KEY = "session:%s";
//    final static String REQUEST_SESSION_RENEWAL_KEY = "#session_renewal_state#";
//    final static Integer SESSION_TIMEOUT = 30 * 60;
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//
//    static Logger logger = LoggerFactory.getLogger(BizSecurityContextRepository.class);
//
//    @Override
//    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
//        HttpServletRequest request = requestResponseHolder.getRequest();
//        HttpSession session = request.getSession(false);
//        if (session == null) return null;
//        String cacheKey = String.format(SESSION_KEY, session.getId());
//        SecurityContext securityContext = (SecurityContext) redisTemplate.opsForValue().get(cacheKey);
//        if (securityContext != null && request.getAttribute(REQUEST_SESSION_RENEWAL_KEY) == null) {
//            redisTemplate.expire(cacheKey, SESSION_TIMEOUT, TimeUnit.SECONDS);
//            request.setAttribute(REQUEST_SESSION_RENEWAL_KEY, true);
//        }
//        return securityContext;
//    }
//
//    @Override
//    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
//        return SecurityContextRepository.super.loadDeferredContext(request);
//    }
//
//    @Override
//    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
//        if (context == null) {
//            return;
//        } else if (context.getAuthentication() == null) {
//            // 登出
//            HttpSession session = request.getSession(false);
//            if (session != null) {
//                String cacheKey = String.format(SESSION_KEY, session.getId());
//                redisTemplate.delete(cacheKey);
//            }
//        } else {
//            if (context.getAuthentication().isAuthenticated()) {
//                HttpSession session = request.getSession(true);
//                String cacheKey = String.format(SESSION_KEY, session.getId());
//                // 登录-会话存储
//                redisTemplate.opsForValue().set(cacheKey, context, SESSION_TIMEOUT, TimeUnit.SECONDS);
//            }
//        }
//    }
//
//    @Override
//    public boolean containsContext(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null) return false;
//        String cacheKey = String.format(SESSION_KEY, session.getId());
//        return redisTemplate.hasKey(cacheKey);
//    }
//
//}
