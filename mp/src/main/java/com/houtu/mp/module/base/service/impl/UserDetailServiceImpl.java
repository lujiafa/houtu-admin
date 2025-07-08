package com.houtu.mp.module.base.service.impl;

import com.houtu.core.exception.BusinessException;
import com.houtu.core.exception.ErrorCode;
import com.houtu.mp.config.security.SecuritySupport;
import com.houtu.mp.config.security.SimpleUser;
import com.houtu.mp.module.sys.dao.SysMenuDao;
import com.houtu.mp.module.sys.dao.SysRoleDao;
import com.houtu.mp.module.sys.entity.SysMenuEntity;
import com.houtu.mp.module.sys.entity.SysRoleEntity;
import com.houtu.mp.module.sys.entity.SysUserEntity;
import com.houtu.mp.module.sys.service.SysUserService;
import com.houtu.mp.support.type.CommonStatus;
import com.houtu.util.web.WebUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    final static String LIMIT_FREQUENCY_KEY = "common:frequency:limit:%s:%d:%s";

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleDao sysRoleDao;
    @Resource
    private SysMenuDao sysMenuDao;

    @Resource
    private Environment environment;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        frequencyLimitCheck();
        SysUserEntity sysUserEntity = sysUserService.findByUsername(username);
        if (sysUserEntity == null) {
            throw new UsernameNotFoundException("user is not exists");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        boolean admin = false; // 是否是超级管理员
        List<SysRoleEntity> sysRoleEntityList = sysRoleDao.queryUserRoleList(sysUserEntity.getUserId(), CommonStatus.ENABLED.getStatus());
        if (sysRoleEntityList != null && !sysRoleEntityList.isEmpty()) {
            List<String> rolePerms = sysRoleEntityList.parallelStream().filter(r -> StringUtils.isNotBlank(r.getRolePerms())).map(r -> r.getRolePerms()).toList();
            admin = rolePerms.parallelStream().anyMatch(p -> SecuritySupport.hasAdmin(p));
            if (!admin) {
                authorities.addAll(rolePerms.parallelStream().map(p -> new SimpleGrantedAuthority(p)).toList());
                List<SysMenuEntity> sysMenuEntities = sysMenuDao.queryMenuByRoleIds(sysRoleEntityList.parallelStream().map(SysRoleEntity::getRoleId).toList(), CommonStatus.ENABLED.getStatus(), null);
                if (sysMenuEntities != null && !sysMenuEntities.isEmpty()) {
                    authorities.addAll(sysMenuEntities.parallelStream().filter(p -> StringUtils.isNotBlank(p.getPerms())).map(m -> new SimpleGrantedAuthority(m.getPerms())).collect(Collectors.toSet()));
                }
            }
        }
        SimpleUser simpleUser = new SimpleUser(sysUserEntity.getUserName(), sysUserEntity.getPassword(), Objects.equals(sysUserEntity.getStatus(), 1), true, true, true, admin, authorities);
        simpleUser.setUserId(sysUserEntity.getUserId());
        simpleUser.setEnableMFA(BooleanUtils.toBoolean(sysUserEntity.getEnableMFA()));
        return simpleUser;
    }

    void frequencyLimitCheck() {
        ServletRequestAttributes requestAttributes = WebUtils.getServletRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String requestIpChain = WebUtils.getRequestIpChain(request);
        String limitCacheKey = String.format(LIMIT_FREQUENCY_KEY, environment.getProperty("application.name"), 1, requestIpChain);
        Long num = stringRedisTemplate.opsForValue().increment(limitCacheKey);
        if (num <= 1)
            stringRedisTemplate.expire(limitCacheKey, 180, java.util.concurrent.TimeUnit.SECONDS);
        if (num > 5)
            throw new BusinessException(ErrorCode.build(14, Stream.of("您的操作太频繁，请3分钟后再试").toArray()));
    }
}
