package com.houtu.mp;

import com.houtu.mp.module.sys.dao.SysUserDao;
import com.houtu.mp.module.sys.entity.SysUserEntity;
import com.houtu.util.crypto.HMacSHAUtils;
import com.houtu.util.data.HexUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
public class UserTests {

    @Resource
    private PasswordEncoder pwdEncoder;
    @Resource
    private SysUserDao userDao;

    @Test
    public void updatePassword() {
        String pwd = "admin123";
        String encPwd = HexUtils.toHex(HMacSHAUtils.encryptHMacSHA256(pwd.getBytes(), pwd.getBytes()));
        SysUserEntity updateEntity = new SysUserEntity();
        updateEntity.setUserId(1L);
        updateEntity.setPassword(pwdEncoder.encode(encPwd));
        updateEntity.setUpdateBy(0L);
        updateEntity.setUpdateTime(LocalDateTime.now());
        userDao.updateById(updateEntity);

    }
    // create database sysbase charset=utf8 collate=utf8_general_ci;
}
