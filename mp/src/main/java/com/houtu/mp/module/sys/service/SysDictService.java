package com.houtu.mp.module.sys.service;

import com.houtu.mp.module.sys.entity.SysDictEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.request.SysDictAddRequest;
import com.houtu.mp.module.sys.request.SysDictQueryRequest;
import com.houtu.mp.module.sys.request.SysDictUpdateRequest;
import com.houtu.mp.module.sys.vo.SysDictQueryVO;
import com.houtu.web.model.response.ResponseData;
import com.houtu.web.model.vo.PageDataVO;

import java.util.List;

/**
 * <p>
 * sys_dict 服务类
 * </p>
 *
 * @author jonlu
 * @since 2024-09-06
 */
public interface SysDictService extends IService<SysDictEntity> {

    ResponseData<PageDataVO<SysDictQueryVO>> pageQuery(SysDictQueryRequest request);

    ResponseData save(SysDictAddRequest request);

    ResponseData update(SysDictUpdateRequest request);

    ResponseData delete(List<Long> dictIds);

}
