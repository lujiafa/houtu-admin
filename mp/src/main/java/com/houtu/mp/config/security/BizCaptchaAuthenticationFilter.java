package com.houtu.mp.config.security;

import com.houtu.mp.util.ResponseUtils;
import io.github.lujiafa.houtu.core.exception.ErrorCode;
import io.github.lujiafa.houtu.web.model.ResponseData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.stream.Stream;

public class BizCaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public final static String CAPTCHA_CACHE_PATTERN = "captcha:verify:%s";
    public final static String CAPTCHA_TOKEN_NAME = "kaptcha-token";

    private StringRedisTemplate redisTemplate;

    public BizCaptchaAuthenticationFilter(String loginProcessingUrl, StringRedisTemplate redisTemplate) {
        super(new AntPathRequestMatcher(loginProcessingUrl, "POST"));
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        if (!requiresAuthentication(servletRequest, (HttpServletResponse) response)) {
            chain.doFilter(request, response);
            return;
        }
        Cookie[] cookies = servletRequest.getCookies();
        String captcha = request.getParameter("captcha");
        if (cookies == null || StringUtils.isBlank(captcha)) {
            ResponseUtils.responseJson(servletResponse, ResponseData.fail(ErrorCode.build(17, LocaleContextHolder.getLocale())));
            return;
        }
        boolean match = Stream.of(cookies).anyMatch(p -> {
            if (CAPTCHA_TOKEN_NAME.equals(p.getName())) {
                String cacheKey = String.format(CAPTCHA_CACHE_PATTERN, p.getValue());
                String cacheValue = redisTemplate.opsForValue().get(cacheKey);
                if (cacheValue != null && captcha.trim().equalsIgnoreCase(cacheValue.trim())) {
                    return true;
                }
            }
            return false;
        });
        if (!match) {
            ResponseUtils.responseJson(servletResponse, ResponseData.fail(ErrorCode.build(17, servletRequest.getLocale())));
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
