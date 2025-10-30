package com.houtu.mp.config;

import com.houtu.core.exception.BusinessException;
import com.houtu.mp.module.base.service.LoginLogService;
import com.houtu.mp.support.type.LoginType;
import com.houtu.mp.util.ResponseUtils;
import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.config.security.BizCaptchaAuthenticationFilter;
import com.houtu.mp.config.security.BizSecurityContextRepository;
import com.houtu.mp.config.security.MFAAuthorizationManager;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.web.model.ResponseData;
import javax.servlet.DispatcherType;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SpringSecurityConfig {

    static Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   BizSecurityContextRepository securityContextRepository,
                                                   StringRedisTemplate stringRedisTemplate,
                                                   SecurityProperties securityProperties,
                                                   LoginLogService loginLogService) throws Exception {
        // https://docs.spring.io/spring-security/reference/6.3-SNAPSHOT/servlet/exploits/firewall.html#page-title
        // https://docs.spring.io/spring-security/reference/6.3-SNAPSHOT/servlet/architecture.html#servlet-securityfilterchain
        http.csrf(csrfConfigurer -> csrfConfigurer.disable())
                .authorizeHttpRequests(authorize -> { // 授权过滤器配置
                    authorize.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                            .antMatchers("/api/login", "/api/getCaptcha").permitAll();
                    if (securityProperties.isMfa()) {
                        // 启用MFA
                        authorize.antMatchers("/api/mfa/mfaTypes", "/api/mfa/send", "/api/mfa/verify").permitAll()
                                .antMatchers("/api/**").access(new MFAAuthorizationManager(AuthenticatedAuthorizationManager.authenticated()));
                    } else {
                        authorize.antMatchers("/api/**").authenticated();
                    }
                    authorize.anyRequest().denyAll();
                })
                .formLogin(formLoginConfigurer -> {
                    formLoginConfigurer.loginProcessingUrl("/api/login")
                            // 登录认证成功
                            .successHandler((req, resp, auth) -> {
                                if (securityProperties.isMfa() && ((SimpleUser) auth.getPrincipal()).isEnableMFA()) {
                                    ResponseUtils.responseJson(resp, ResponseData.fail(ErrorCode.build(13, req.getLocale())));
                                } else {
                                    ResponseUtils.responseJson(resp, ResponseData.success(req.getLocale()));
                                    // 登录成功记录登录日志
                                    loginLogService.log(req, LoginType.LOGIN, auth.getName());
                                }
                            })
                            // 登录认证失败
                            .failureHandler((req, resp, ex) -> {
                                Optional exceptionOptional = Arrays.stream(ExceptionUtils.getThrowables(ex)).filter(e -> e instanceof BusinessException).findFirst();
                                if (!exceptionOptional.isPresent()) {
                                    ResponseUtils.responseJson(resp, ResponseData.fail(ErrorCode.build(12, req.getLocale())));
                                } else {
                                    ResponseUtils.responseJson(resp, ResponseData.fail(((BusinessException) exceptionOptional.get()).getErrorCode()));
                                }
                            })
                    ;
                })
                .addFilterBefore(new BizCaptchaAuthenticationFilter("/api/login", stringRedisTemplate), UsernamePasswordAuthenticationFilter.class)
                .logout(logoutConfigurer -> {
                    logoutConfigurer.logoutUrl("/api/logout")
                            .addLogoutHandler((req, resp, auth) -> {
                                if (auth != null && auth.getPrincipal() != null) {
                                    // 登出记录日志
                                    loginLogService.log(req, LoginType.LOGOUT, auth.getName());
                                }
                            })
                            // 登出成功
                            .logoutSuccessHandler((req, resp, auth) -> {
                                ResponseUtils.responseJson(resp, ResponseData.success(req.getLocale()));
                            });
                })
                .securityContext(securityContextConfigurer -> {
                    securityContextConfigurer
                            // 安全上下文持久化处理器
                            .securityContextRepository(securityContextRepository);
                })
                .exceptionHandling(exceptionConfigurer -> {
                    exceptionConfigurer
                            // 认证验证失败处理器
                            .authenticationEntryPoint((req, resp, ex) -> {
                                ResponseUtils.responseJson(resp, ResponseData.fail(ErrorCode.build(15, req.getLocale())));
                            })
                            // 访问权限验证失败处理器
                            .accessDeniedHandler((req, resp, ex) -> {
                                ResponseUtils.responseJson(resp, ResponseData.fail(ErrorCode.build(19, req.getLocale())));
                            });
                });
        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
