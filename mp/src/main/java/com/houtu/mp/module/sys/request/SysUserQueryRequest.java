package com.houtu.mp.module.sys.request;

import io.github.lujiafa.houtu.web.model.form.PageForm;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class SysUserQueryRequest extends PageForm {

    private String orgIds;

    private String userName;

    private String nickName;

    private Integer status;

    public List<Long> getParseOrgIds() {
        if (orgIds == null) {
            return List.of();
        }
        List<Long> newOrgIds = new ArrayList<>();
        String[] strArray = orgIds.split(",");
        Arrays.stream(strArray).forEach(p -> {
            if (StringUtils.isNotBlank(p)) {
                try {
                    newOrgIds.add(Long.valueOf(p.trim()));
                } catch (Exception ignored) {}
            }
        });
        return newOrgIds;
    }
}
