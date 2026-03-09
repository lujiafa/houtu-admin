package com.xx.mp.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import io.github.lujiafa.houtu.util.crypto.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public final class OTPUtils {

    final static String IMG_BASE64_PREFIX = "data:image/png;base64,";

    final static GoogleAuthenticator googleAuthenticator;

    static {
        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder googleAuthenticatorConfigBuilder = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();
        GoogleAuthenticatorConfig googleAuthenticatorConfig = googleAuthenticatorConfigBuilder.build();
        googleAuthenticator = new GoogleAuthenticator(googleAuthenticatorConfig);
    }

    public static String generateSecretKey() {
        return googleAuthenticator.createCredentials().getKey();
    }

    public static String generateQRCodeContent(String username, String secretKey) {
        return String.format("otpauth://totp/%s?secret=%s", username, secretKey);
    }

    public static String generateQRCode(String username, String secretKey) {
        try {
            String googleOtpUrl = generateQRCodeContent(username, secretKey);
            int width = 200;
            int height = 200;
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(googleOtpUrl, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            return IMG_BASE64_PREFIX + Base64Utils.encode(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean checkCode(String secretKey, int code) {
        return googleAuthenticator.authorize(secretKey, code);
    }

}
