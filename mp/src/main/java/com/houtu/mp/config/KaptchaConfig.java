/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.houtu.mp.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.text.impl.DefaultTextCreator;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;


/**
 * 生成验证码配置
 *
 * @author Mark sunlightcs@gmail.com
 * @since 2.1.0 2017-04-20
 */
@Configuration
public class KaptchaConfig {

    @Bean(name = "defaultKaptcha")
    public DefaultKaptcha textKaptcha() {
        Properties properties = new Properties();
        // 是否有边框 默认为true 可以设置为yes，no
        properties.put(KAPTCHA_BORDER, "no");
        // 验证码文本字符颜色 默认为Color.BLACK
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // 验证码图片宽度 默认为200
        properties.put(KAPTCHA_IMAGE_WIDTH, "200");
        // 验证码图片高度 默认50
        properties.put(KAPTCHA_IMAGE_HEIGHT, "50");
        // 验证码文本字符大小 默认为40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");
        // 验证码文本生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, CompositeTextCreator.class.getName());
        // 字符集
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_STRING, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        // 验证码文本字符长度 默认为5
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "5");
        // 验证码文本字符间距 默认为2
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "6");
        // 干扰实现类
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.DefaultNoise");
        // 图片样式 水纹:com.google.code.kaptcha.impl.WaterRipple 鱼眼:com.google.code.kaptcha.impl.FishEyeGimpy 阴影:com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    public static class CompositeTextCreator extends DefaultTextCreator {
        @Override
        public String getText() {
            if ((int) (Math.random() * 10) % 2 == 0) {
                return super.getText();
            }
            StringBuilder sb = new StringBuilder();
            int code1 = (int) (Math.random() * 10);
            sb.append(code1);
            int code2 = (int) (Math.random() * 10);
            int m = code2 != 0 && code1 % code2 == 0 ? 4 : 3;
            switch (((int) (Math.random() * 10) % m)) {
                case 0:
                    sb.append("+").append(code2).append("=?@").append(code1 + code2);
                    break;
                case 1:
                    sb.append("-").append(code2).append("=?@").append(code1 - code2);
                    break;
                case 2:
                    sb.append("x").append(code2).append("=?@").append(code1 * code2);
                    break;
                case 3:
                    sb.append("÷").append(code2).append("=?@").append(code1 / code2);
                    break;
            }
            return sb.toString();
        }
    }
}
