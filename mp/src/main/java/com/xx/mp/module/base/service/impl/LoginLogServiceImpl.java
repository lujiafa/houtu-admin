package com.xx.mp.module.base.service.impl;

import com.xx.mp.module.base.service.LoginLogService;
import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.sys.dao.SysLoginLogDao;
import com.xx.mp.module.sys.entity.SysLoginLogEntity;
import com.xx.mp.support.SessionContext;
import com.xx.mp.support.type.LoginType;
import io.github.lujiafa.houtu.util.web.WebUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private SysLoginLogDao loginLogDao;

    @Async
    @Override
    public void log(HttpServletRequest request, LoginType loginType, String username) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        SysLoginLogEntity sysLoginLogEntity = new SysLoginLogEntity();
        sysLoginLogEntity.setUserId(sessionUser.getUserId());
        sysLoginLogEntity.setUserName(username);
        sysLoginLogEntity.setIp(WebUtils.getRequestIp(request));
        // 待接入ip转地理位置
        sysLoginLogEntity.setLocation("");
        String userAgent = request.getHeader("User-Agent");
        sysLoginLogEntity.setUserAgent(userAgent);
        sysLoginLogEntity.setDeviceType(detectDeviceType(userAgent));
        sysLoginLogEntity.setLoginType(loginType.getLoginType());
        sysLoginLogEntity.setLoginTypeDesc(loginType.getLoginTypeDesc());
        sysLoginLogEntity.setCreateTime(LocalDateTime.now());
        loginLogDao.insert(sysLoginLogEntity);
    }


    private String detectDeviceType(String userAgent) {
        if (userAgent.contains("Mobile")) {
            return "Mobile";
        } else if (userAgent.contains("tablet")) {
            return "Tablet";
        } else if (userAgent.contains("Macintosh") || userAgent.contains("Mac os x")) {
            return "Mac";
        } else if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("Iphone") || userAgent.contains("Ipod")) {
            return "iPhone/iPod";
        } else if (userAgent.contains("Ipad")) {
            return "iPad";
        } else {
            return "Unknown";
        }
    }
}
