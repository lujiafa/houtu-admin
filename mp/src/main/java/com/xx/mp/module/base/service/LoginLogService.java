package com.xx.mp.module.base.service;

import com.xx.mp.support.type.LoginType;
import javax.servlet.http.HttpServletRequest;

public interface LoginLogService {

    void log(HttpServletRequest request, LoginType loginType, String username);
}
