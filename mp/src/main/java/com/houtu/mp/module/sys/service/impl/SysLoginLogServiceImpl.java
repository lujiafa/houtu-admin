package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.mp.module.sys.service.SysLoginLogService;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.module.sys.dao.SysLoginLogDao;
import com.houtu.mp.module.sys.dao.SysUserDao;
import com.houtu.mp.module.sys.entity.SysLoginLogEntity;
import com.houtu.mp.module.sys.request.SysLoginQueryRequest;
import com.houtu.mp.module.sys.vo.SysLoginLogQueryVO;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * sys_login_log 服务实现类
 * </p>
 *
 * @author jonlu
 * @since 2024-07-24
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogDao, SysLoginLogEntity> implements SysLoginLogService {

    @Resource
    private SysUserDao sysUserDao;

    @Override
    public ResponseData<PageDataVO<SysLoginLogQueryVO>> pageQuery(SysLoginQueryRequest request) {
        SimpleUser sessionUser = SessionContext.getSessionUser();
        Page<SysLoginLogEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        page = baseMapper.selectPage(page, new QueryWrapper<SysLoginLogEntity>()
                .eq(!sessionUser.isAdmin(), "user_id", SessionContext.getSessionUserId())
                .ne(request.getBeginCreateTime() != null , "create_time", request.getBeginCreateTime())
                .le(request.getEndCreateTime() != null , "create_time", request.getEndCreateTime())
                .orderByDesc("create_time"));
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            return ResponseData.success(PageDataVO.build(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords().stream().map(p -> {
                SysLoginLogQueryVO vo = new SysLoginLogQueryVO();
                BeanUtils.copyProperties(p, vo);
                return vo;
            }).toList()));
        }
        return ResponseData.success(PageDataVO.empty());
    }


}
