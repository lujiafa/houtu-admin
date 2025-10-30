package com.houtu.mp.module.base.service;

import com.houtu.mp.support.type.LoginType;
import javax.servlet.http.HttpServletRequest;

public interface LoginLogService {

    void log(HttpServletRequest request, LoginType loginType, String username);
}
