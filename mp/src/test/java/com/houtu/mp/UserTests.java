package com.houtu.mp;

import com.houtu.mp.module.sys.dao.SysUserDao;
import com.houtu.mp.module.sys.entity.SysUserEntity;
import io.github.lujiafa.houtu.util.common.CodecData;
import io.github.lujiafa.houtu.util.crypto.HMacSHAUtils;
import io.github.lujiafa.houtu.util.crypto.type.HmacSHAAlgorithm;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
public class UserTests {

    @Resource
    private PasswordEncoder pwdEncoder;
    @Resource
    private SysUserDao userDao;

    @Test
    public void updatePassword() {
        String pwd = "admin123";
        String encPwd = HMacSHAUtils.hash(CodecData.ascii(pwd), CodecData.ascii(pwd), HmacSHAAlgorithm.HMAC_SHA_1).hex();
        SysUserEntity updateEntity = new SysUserEntity();
        updateEntity.setUserId(1L);
        updateEntity.setPassword(pwdEncoder.encode(encPwd));
        updateEntity.setUpdateBy(0L);
        updateEntity.setUpdateTime(LocalDateTime.now());
        userDao.updateById(updateEntity);

    }
    // create database sysbase charset=utf8 collate=utf8_general_ci;


    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        System.out.println(list.parallelStream().allMatch(p -> "wakk".equals(p)));
    }
}
