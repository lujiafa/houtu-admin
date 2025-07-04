package com.houtu.mp.module.base.request;

import com.houtu.web.model.BaseForm;
import lombok.Data;

@Data
public class UserInfoUpdateRequest extends BaseForm {

    private String nickName;

    private String phone;

    private String email;
}
