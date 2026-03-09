package com.xx.mp.aspect;

import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.sys.entity.SysOperateLogEntity;
import com.xx.mp.module.sys.service.SysOperateLogService;
import com.xx.mp.support.SessionContext;
import io.github.lujiafa.houtu.util.common.JsonUtils;
import io.github.lujiafa.houtu.util.web.WebUtils;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.util.WebCombineParametersSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Component
@Aspect
public class OperateLogAspect {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(OperateLogAspect.class);

    @Resource
    private SysOperateLogService operateLogService;

    @Pointcut("@annotation(com.xx.mp.aspect.OperateLog)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Method method = getMethod(pjp);
        OperateLog annotation = method.getAnnotation(OperateLog.class);
        ServletRequestAttributes servletRequestAttributes = WebUtils.getServletRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        if (annotation == null
                || request == null) {
            return pjp.proceed(args);
        }
        long inTime = System.currentTimeMillis();
        String responseData = null;
        try {
            Object proceed = pjp.proceed(args);
            if (!annotation.ignoreResponseData()) {
                if (proceed instanceof Void) {
                    responseData = Void.TYPE.getName();
                } else if (proceed instanceof ResponseData) {
                    responseData = JsonUtils.toStringIgnoreNull(proceed);
                }
            }
            return proceed;
        } catch (Throwable e) {
            responseData = e.getMessage();
            throw e;
        } finally {
            try {
                SimpleUser sessionUser = SessionContext.getSessionUser();
                SysOperateLogEntity operateLog = new SysOperateLogEntity();
                operateLog.setModuleName(annotation.moduleType().getModuleName());
                operateLog.setOperateType(annotation.operateType().getOperateType());
                operateLog.setRequestMethod(request.getMethod());
                operateLog.setMethod(method.getName());
                operateLog.setUserId(sessionUser.getUserId());
                operateLog.setIp(WebUtils.getRequestIp(request));
                operateLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
                operateLog.setRequestParams(getRequestParams(request, response, args));
                operateLog.setResponseData(responseData);
                LocalDateTime currentTime = LocalDateTime.now();
                long finishTime = currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                operateLog.setCostTime(finishTime - inTime);
                operateLog.setCreateTime(currentTime);
                operateLogService.save(operateLog);
            } catch (Exception e) {
                logger.error("save operateLog error：{}", e.getMessage(), e);
            }
        }
    }

    private String getRequestParams(HttpServletRequest request, HttpServletResponse response, Object[] args) {
        try {
            Map params = WebCombineParametersSupport.getCombineParameterMap(request, response);
            return JsonUtils.toString(params);
        } catch (Exception e) {
        }
        try {
            return JsonUtils.toString(WebUtils.getUrlEncodedParams(request));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method;
    }

}