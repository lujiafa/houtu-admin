package com.xx.mp.module.base.service;

import com.xx.mp.module.base.request.MFASendRequest;
import com.xx.mp.module.base.request.MFAVerifyRequest;
import com.xx.mp.module.base.request.UserInfoUpdateRequest;
import com.xx.mp.module.base.request.UserUpdateMyselfPasswordRequest;
import com.xx.mp.module.base.vo.UserInfoVO;
import io.github.lujiafa.houtu.web.model.ResponseData;

public interface UserService {

    /**
     * 获取当前用户信息
     * @return UserInfoVO 用户信息
     */
    UserInfoVO info();


    /**
     * 修改当前登录用户信息
     * @param request 修改信息
     * @return ResponseData 修改结果
     */
    ResponseData updateUserInfo(UserInfoUpdateRequest request);


    /**
     * 修改当前登录用户的密码
     * @param request 修改密码
     * @return ResponseData 修改结果
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
