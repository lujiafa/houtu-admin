package com.houtu.mp.module.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lujiafa.houtu.web.model.BaseVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserInfoVO extends BaseVO {

    private String userName;

    private String nickName;

    private String avatar;

    private String phone;

    private String email;

    private List<String> roleNames;

    private List<String> orgNames;

    private List<String> orgPosts;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
