package com.houtu.mp.support;

import com.houtu.mp.config.security.SimpleUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionContext {

    public static SimpleUser getSessionUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new InvalidCookieException("会话获取失败");
        }
        return (SimpleUser) securityContext.getAuthentication().getPrincipal();
    }

    public static void update(HttpServletRequest  request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null && securityContext.getAuthentication() != null && securityContext.getAuthentication().getPrincipal() instanceof SimpleUser) {
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        }
    }

    public static Long getSessionUserId() {
        return getSessionUser().getUserId();
    }

    public static boolean isAdmin() {
        return getSessionUser().isAdmin();
    }
}
