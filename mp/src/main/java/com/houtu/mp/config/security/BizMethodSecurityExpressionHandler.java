package com.houtu.mp.config.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 自定义权限表达式处理器
 */
@Component
public class BizMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    @Override
    public StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, MethodInvocation mi) {
        StandardEvaluationContext context = super.createEvaluationContextInternal(authentication, mi);
        MethodSecurityExpressionOperations delegate = (MethodSecurityExpressionOperations) context.getRootObject().getValue();
        BizSecurityExpressionRoot root = new BizSecurityExpressionRoot(delegate);
        context.setRootObject(root);
        return context;
    }

    static class BizSecurityExpressionRoot implements SecurityExpressionOperations {
        private SecurityExpressionOperations delegate;

        public BizSecurityExpressionRoot(SecurityExpressionOperations delegate) {
            this.delegate = delegate;
        }

        @Override
        public Authentication getAuthentication() {
            return delegate.getAuthentication();
        }

        @Override
        public boolean hasAuthority(String authority) {
            return isAdmin() || delegate.hasAuthority(authority);
        }

        @Override
        public boolean hasAnyAuthority(String... authorities) {
            return isAdmin() || delegate.hasAnyAuthority(authorities);
        }

        @Override
        public boolean hasRole(String role) {
            return isAdmin() || delegate.hasRole(role);
        }

        @Override
        public boolean hasAnyRole(String... roles) {
            return isAdmin() || delegate.hasAnyRole(roles);
        }

        @Override
        public boolean permitAll() {
            return isAdmin() || delegate.permitAll();
        }

        @Override
        public boolean denyAll() {
            return isAdmin() || delegate.denyAll();
        }

        @Override
        public boolean isAnonymous() {
            return isAdmin() || delegate.isAnonymous();
        }

        @Override
        public boolean isAuthenticated() {
            return isAdmin() || delegate.isAuthenticated();
        }

        @Override
        public boolean isRememberMe() {
            return isAdmin() || delegate.isRememberMe();
        }

        @Override
        public boolean isFullyAuthenticated() {
            return isAdmin() || delegate.isFullyAuthenticated();
        }

        @Override
        public boolean hasPermission(Object target, Object permission) {
            return isAdmin() || delegate.hasPermission(target, permission);
        }

        @Override
        public boolean hasPermission(Object targetId, String targetType, Object permission) {
            return isAdmin() || delegate.hasPermission(targetId, targetType, permission);
        }

        private boolean isAdmin() {
            Object principal = getAuthentication().getPrincipal();
            if (principal instanceof SimpleUser) {
                return ((SimpleUser) principal).isAdmin();
            }
            return false;
        }
    }
}
