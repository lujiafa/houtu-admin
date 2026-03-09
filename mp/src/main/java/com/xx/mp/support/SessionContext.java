package com.xx.mp.support;

import com.xx.mp.config.security.SimpleUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;


public final class SessionContext {

    public static SimpleUser getSessionUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null) {
            throw new InvalidCookieException("会话获取失败");
        }
        return (SimpleUser) securityContext.getAuthentication().getPrincipal();
    }

    public static void update(HttpServletRequest request) {
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
