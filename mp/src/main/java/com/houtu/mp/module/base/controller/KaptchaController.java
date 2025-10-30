package com.houtu.mp.module.base.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.houtu.mp.config.security.BizCaptchaAuthenticationFilter;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class KaptchaController {

    @Resource
    private DefaultKaptcha defaultKaptcha;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/getCaptcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String verifyToken = UUID.randomUUID().toString().replaceAll("-", "");
        String kaptchaText = defaultKaptcha.createText();
        String[] kaptchaArray = kaptchaText.split("@");
        String codeContent = kaptchaArray[0];
        String codeResult = kaptchaArray.length > 1 ? kaptchaArray[1] : kaptchaArray[0];
        BufferedImage imgBuffered = defaultKaptcha.createImage(codeContent);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("expires", -1);
        response.setContentType("image/jpeg");
        String cacheKey = String.format(BizCaptchaAuthenticationFilter.CAPTCHA_CACHE_PATTERN, verifyToken);
        Cookie cookie = new Cookie(BizCaptchaAuthenticationFilter.CAPTCHA_TOKEN_NAME, verifyToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        stringRedisTemplate.opsForValue().set(cacheKey, codeResult, 60, TimeUnit.SECONDS);
        ImageIO.write(imgBuffered, "jpg", response.getOutputStream());
    }
}
