package com.houtu.mp.module.base.vo;

import com.houtu.web.model.BaseVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVO extends BaseVO {

    private String nickName;

    private String avatar;

    private String phone;

    private String email;
}
