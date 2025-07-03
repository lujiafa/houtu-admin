package com.houtu.mp.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houtu.mp.module.sys.entity.SysPostEntity;
import com.houtu.mp.module.sys.request.SysPostAddRequest;
import com.houtu.mp.module.sys.request.SysPostAuthorizeRequest;
import com.houtu.mp.module.sys.request.SysPostQueryRequest;
import com.houtu.mp.module.sys.request.SysPostUpdateRequest;
import com.houtu.web.model.vo.PageDataVO;
import com.houtu.mp.module.sys.vo.SysPostQueryBaseVO;
import com.houtu.mp.module.sys.vo.SysPostQueryVO;
import com.houtu.web.model.response.ResponseData;

import java.util.List;

/**
 * <p>
 * sys_post 服务类
 * </p>
 *
 * @author houtu
 * @since 2024-06-24
 */
public interface SysPostService extends IService<SysPostEntity> {

    /**
     * 根据条件查询岗位列表，仅返回前1000条数据
     * @param request
     * @return
     */
    List<SysPostQueryBaseVO> queryBaseList(SysPostQueryRequest request);

    ResponseData<PageDataVO<SysPostQueryVO>> pageQuery(SysPostQueryRequest request);

    ResponseData save(SysPostAddRequest request);

    ResponseData update(SysPostUpdateRequest request);

    ResponseData authorize(SysPostAuthorizeRequest request);

    ResponseData delete(List<Long> postIds);

}
