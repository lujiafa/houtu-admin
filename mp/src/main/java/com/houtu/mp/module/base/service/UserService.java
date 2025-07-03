package com.houtu.mp.module.base.service;

import com.houtu.mp.module.base.request.MFASendRequest;
import com.houtu.mp.module.base.request.MFAVerifyRequest;
import com.houtu.mp.module.base.request.UserUpdateMyselfPasswordRequest;
import com.houtu.web.model.response.ResponseData;

public interface UserService {



    /**
     * 修改当前登录用户的密码
     * @param request
     * @return ResponseData
     */
    ResponseData updateMyselfPassword(UserUpdateMyselfPasswordRequest request);

    /**
     * 发送MFA验证码
     * @param request MFA验证码发送请求
     * @return ResponseData 发送结果
     */
    ResponseData sendMFA(MFASendRequest request);

    /**
     * 验证MFA
     * @param request MFA验证请求
     * @return ResponseData 验证结果
     */
    ResponseData verifyMFA(MFAVerifyRequest request);

    /**
     * 开启MFA
     * @return ResponseData 开启结果
     */
    ResponseData openMFA();

    /**
     * 获取当前登录用户的OTP二维码Img Base64数据
     * @return ResponseData OTP二维码Img Base64数据
     */
    ResponseData<String> myselfOTP();

    /**
     * 重置当前登录用户的OTP密钥并返回新的OTP二维码Img Base64数据
     * @return ResponseData 新的OTP二维码Img Base64数据
     */
    ResponseData<String> resetMyselfOTP();
}
