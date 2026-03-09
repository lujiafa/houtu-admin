package com.xx.mp.config.security;

import org.springframework.core.Ordered;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class MFAAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext>, Ordered {

    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);

    private AuthorizationManager<RequestAuthorizationContext> delegate;

    public MFAAuthorizationManager(AuthorizationManager<RequestAuthorizationContext> delegate) {
        this.delegate = delegate;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext object) {
        AuthorizationDecision decision = delegate.check(authenticationSupplier, object);
        SimpleUser user;
        if (decision != null
                && decision.isGranted()
                && (user = (SimpleUser) authenticationSupplier.get().getPrincipal()).isEnableMFA()
                && !user.isMfaPassed()) {
            return new AuthorizationDecision(false);
        }
        return decision;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        delegate.verify(authentication, object);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
