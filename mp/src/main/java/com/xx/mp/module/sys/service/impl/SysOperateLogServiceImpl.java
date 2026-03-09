package com.xx.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xx.mp.config.security.SimpleUser;
import com.xx.mp.module.sys.service.SysOperateLogService;
import com.xx.mp.support.SessionContext;
import com.xx.mp.module.sys.dao.SysOperateLogDao;
import com.xx.mp.module.sys.entity.SysOperateLogEntity;
import com.xx.mp.module.sys.request.SysOperateQueryRequest;
import com.xx.mp.module.sys.vo.SysOperateLogQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * sys_operate_log 服务实现类
 * </p>
 *
 * @author jonlu
 * @since 2024-07-24
 */
@Service
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogDao, SysOperateLogEntity> implements SysOperateLogService {

    @Override
    public ResponseData<PageDataVO<SysOperateLogQueryVO>> pageQuery(SysOperateQueryRequest request) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        Page<SysOperateLogEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        page = baseMapper.selectPage(page, new QueryWrapper<SysOperateLogEntity>()
                .eq(!sessionUser.isAdmin(), "user_id", SessionContext.getSessionUserId())
                .ne(request.getBeginCreateTime() != null , "create_time", request.getBeginCreateTime())
                .le(request.getEndCreateTime() != null , "create_time", request.getEndCreateTime())
                .orderByDesc("create_time"));
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            return ResponseData.success(PageDataVO.build(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords().stream().map(p -> {
                SysOperateLogQueryVO vo = new SysOperateLogQueryVO();
                BeanUtils.copyProperties(p, vo);
                return vo;
            }).toList()));
        }
        return ResponseData.success(PageDataVO.empty());
    }
}
