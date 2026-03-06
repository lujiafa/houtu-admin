package com.houtu.mp.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.lujiafa.houtu.core.exception.ErrorCode;
import com.houtu.mp.config.SecurityProperties;
import com.houtu.mp.config.security.SecuritySupport;
import com.houtu.mp.module.sys.dao.*;
import com.houtu.mp.module.sys.entity.*;
import com.houtu.mp.module.sys.request.SysUserAddRequest;
import com.houtu.mp.module.sys.request.SysUserAuthorizeRequest;
import com.houtu.mp.module.sys.request.SysUserQueryRequest;
import com.houtu.mp.module.sys.request.SysUserUpdateRequest;
import com.houtu.mp.module.sys.service.SysUserService;
import com.houtu.mp.module.sys.vo.SysUserQueryVO;
import com.houtu.mp.module.sys.vo.SysUserSecretVO;
import com.houtu.mp.support.SessionContext;
import com.houtu.mp.support.type.CommonStatus;
import com.houtu.mp.util.OTPUtils;
import io.github.lujiafa.houtu.util.common.CodecData;
import io.github.lujiafa.houtu.util.crypto.Base64Utils;
import io.github.lujiafa.houtu.util.crypto.HMacMD5Utils;
import io.github.lujiafa.houtu.util.crypto.HMacSHAUtils;
import io.github.lujiafa.houtu.util.data.HexUtils;
import io.github.lujiafa.houtu.web.model.ResponseData;
import io.github.lujiafa.houtu.web.model.vo.PageDataVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * sys_user 服务实现类
 * </p>
 *
 * @author houtu
 * @since 2024-06-21
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    final static String CACHE_USER_BY_USERNAME = "SYS:USER:username:%s";
    final static String CACHE_USER_BY_USERID = "SYS:USER:id:%d";

    @Resource
    private SysUserRoleDao sysUserRoleDao;
    @Resource
    private SysUserOrgDao sysUserOrgDao;
    @Resource
    private SysUserPostDao sysUserPostDao;
    @Resource
    private SysRoleDao sysRoleDao;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private SecurityProperties securityProperties;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public SysUserEntity findById(Long userId) {
        String cacheKey = String.format(CACHE_USER_BY_USERID, userId);
        Object object = redisTemplate.opsForValue().get(cacheKey);
        if (object != null) {
            if (object instanceof SysUserEntity) {
                return (SysUserEntity) object;
            }
            return null;
        }
        SysUserEntity sysUserEntity = baseMapper.selectOne(new QueryWrapper<SysUserEntity>()
                .eq("user_id", userId)
                .eq("deleted", 0)
                .last("limit 1"));
        if (sysUserEntity != null) {
            redisTemplate.opsForValue().set(cacheKey, sysUserEntity, 3, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(cacheKey, "", 10, TimeUnit.SECONDS);
        }
        return sysUserEntity;
    }

    @Override
    public SysUserEntity findByUsername(String username) {
        String cacheKey = String.format(CACHE_USER_BY_USERNAME, username);
        Object object = redisTemplate.opsForValue().get(cacheKey);
        if (object != null) {
            if (object instanceof SysUserEntity) {
                return (SysUserEntity) object;
            }
            return null;
        }
        SysUserEntity sysUserEntity = baseMapper.selectOne(new QueryWrapper<SysUserEntity>()
                .eq("user_name", username)
                .eq("deleted", 0)
                .last("limit 1"));
        if (sysUserEntity != null) {
            redisTemplate.opsForValue().set(cacheKey, sysUserEntity, 3, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(cacheKey, "", 10, TimeUnit.SECONDS);
        }
        return sysUserEntity;
    }

    @Override
    public ResponseData<PageDataVO<SysUserQueryVO>> pageQuery(SysUserQueryRequest request) {
        Page<SysUserEntity> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("orgIds", request.getParseOrgIds());
        queryParams.put("userName", request.getUserName());
        queryParams.put("nickName", request.getNickName());
        queryParams.put("status", request.getStatus());
        page = baseMapper.queryPage(page, queryParams);
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            List<SysUserQueryVO> volist = page.getRecords().stream().map(p -> {
                SysUserQueryVO vo = new SysUserQueryVO();
                BeanUtils.copyProperties(p, vo);
                return vo;
            }).toList();
            List<Long> userIds = volist.stream().map(SysUserQueryVO::getUserId).toList();
            Map<Long, List<SysUserPostEntity>> userPostMap = sysUserPostDao.selectList(new QueryWrapper<SysUserPostEntity>().in("user_id", userIds)).stream().collect(Collectors.groupingBy(SysUserPostEntity::getUserId));
            Map<Long, List<SysUserOrgEntity>> userOrgMap = sysUserOrgDao.selectList(new QueryWrapper<SysUserOrgEntity>().in("user_id", userIds)).stream().collect(Collectors.groupingBy(SysUserOrgEntity::getUserId));
            List<SysUserRoleEntity> userRoleList = sysUserRoleDao.selectList(new QueryWrapper<SysUserRoleEntity>().in("user_id", userIds)).stream().toList();
            Map<Long, List<SysUserRoleEntity>> userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(SysUserRoleEntity::getUserId));
            Map<Long, String> rolePermsMap;
            if (!userRoleList.isEmpty()) {
                rolePermsMap = sysRoleDao.selectList(new QueryWrapper<SysRoleEntity>().in("role_id", userRoleList.stream().map(SysUserRoleEntity::getRoleId).toList())).stream().collect(Collectors.toMap(SysRoleEntity::getRoleId, SysRoleEntity::getRolePerms));
            } else {
                rolePermsMap = null;
            }
            volist.stream().forEach(v -> {
                List<SysUserPostEntity> sysUserPostEntities = userPostMap.get(v.getUserId());
                if (sysUserPostEntities != null) {
                    v.setPostIds(sysUserPostEntities.stream().map(SysUserPostEntity::getPostId).toList());
                }
                List<SysUserOrgEntity> sysUserOrgEntities = userOrgMap.get(v.getUserId());
                if (sysUserOrgEntities != null) {
                    v.setOrgIds(sysUserOrgEntities.stream().map(SysUserOrgEntity::getOrgId).toList());
                }
                List<SysUserRoleEntity> sysUserRoleEntities = userRoleMap.get(v.getUserId());
                if (sysUserRoleEntities != null) {
                    v.setRoleIds(sysUserRoleEntities.stream().map(SysUserRoleEntity::getRoleId).toList());
                    v.setAdmin(sysUserRoleEntities.stream().anyMatch(p -> SecuritySupport.hasAdmin(rolePermsMap.get(p.getRoleId()))));
                }
            });
            return ResponseData.success(PageDataVO.build(page.getCurrent(), page.getSize(), page.getTotal(), volist));
        }
        return ResponseData.success(PageDataVO.empty());
    }

    @Transactional
    @Override
    public ResponseData<SysUserSecretVO> save(SysUserAddRequest request) {
        SysUserEntity userEntity = new SysUserEntity();
        BeanUtils.copyProperties(request, userEntity);
        String originPassword = RandomStringUtils.randomAlphanumeric(10);
        userEntity.setPassword(passwordEncoder.encode(HMacMD5Utils.hash(CodecData.utf8(originPassword), CodecData.utf8(originPassword)).hex().toLowerCase()));
        userEntity.setEnableMFA(BooleanUtils.toInteger(securityProperties.isMfa() && securityProperties.isDefaultEnableMFA()));
        userEntity.setGoogleOTPKey(OTPUtils.generateSecretKey());
        userEntity.setCreateBy(SessionContext.getSessionUserId());
        userEntity.setCreateTime(LocalDateTime.now());
        int num = baseMapper.insert(userEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        if (request.getOrgIds() != null && request.getOrgIds().size() > 0) {
            List<SysUserOrgEntity> userOrgEntities = request.getOrgIds().stream().map(o -> {
                SysUserOrgEntity userOrgEntity = new SysUserOrgEntity();
                userOrgEntity.setOrgId(o);
                userOrgEntity.setUserId(userEntity.getUserId());
                return userOrgEntity;
            }).toList();
            sysUserOrgDao.insert(userOrgEntities);
        }
        if (request.getPostIds() != null && request.getPostIds().size() > 0) {
            List<SysUserPostEntity> userPostEntities = request.getPostIds().stream().map(p -> {
                SysUserPostEntity userPostEntity = new SysUserPostEntity();
                userPostEntity.setPostId(p);
                userPostEntity.setUserId(userEntity.getUserId());
                return userPostEntity;
            }).toList();
            sysUserPostDao.insert(userPostEntities);
        }
        SysUserSecretVO sysUserSecretVO = new SysUserSecretVO();
        sysUserSecretVO.setPassword(Base64Utils.encode(originPassword.getBytes(StandardCharsets.UTF_8)));
        if (BooleanUtils.toBoolean(userEntity.getEnableMFA())) {
            sysUserSecretVO.setOtpImgBase64(OTPUtils.generateQRCode(request.getUserName(), userEntity.getGoogleOTPKey()));
        }
        return ResponseData.success(sysUserSecretVO);
    }

    @Transactional
    @Override
    public ResponseData update(SysUserUpdateRequest request) {
        SysUserEntity userEntity = new SysUserEntity();
        BeanUtils.copyProperties(request, userEntity);
        userEntity.setUpdateBy(SessionContext.getSessionUserId());
        userEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(userEntity);
        if (num <= 0) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
        }
        List<Long> reqOrgIds = request.getOrgIds() == null ? List.of() : request.getOrgIds();
        List<Long> existsOrgIds = sysUserOrgDao.selectList(new QueryWrapper<SysUserOrgEntity>().eq("user_id", request.getUserId())).stream().map(p -> p.getOrgId()).toList();
        List<Long> addOrgIds = reqOrgIds.stream().filter(o -> !existsOrgIds.contains(o)).toList();
        List<Long> delOrgIds = existsOrgIds.stream().filter(o -> !reqOrgIds.contains(o)).toList();

        List<Long> reqPostIds = request.getPostIds() == null ? List.of() : request.getPostIds();
        List<Long> existsPostIds = sysUserPostDao.selectList(new QueryWrapper<SysUserPostEntity>().eq("user_id", request.getUserId())).stream().map(p -> p.getPostId()).toList();
        List<Long> addPostIds = reqPostIds.stream().filter(o -> !existsPostIds.contains(o)).toList();
        List<Long> delPostIds = existsPostIds.stream().filter(o -> !reqPostIds.contains(o)).toList();

        if (addOrgIds.size() > 0) {
            sysUserOrgDao.insert(addOrgIds.stream().map(o -> {
                SysUserOrgEntity userOrgEntity = new SysUserOrgEntity();
                userOrgEntity.setOrgId(o);
                userOrgEntity.setUserId(userEntity.getUserId());
                return userOrgEntity;
            }).toList());
        }
        if (delOrgIds.size() > 0) {
            sysUserOrgDao.delete(new QueryWrapper<SysUserOrgEntity>().eq("user_id", userEntity.getUserId()).in("org_id", delOrgIds));
        }
        if (addPostIds.size() > 0) {
            sysUserPostDao.insert(addPostIds.stream().map(o -> {
                SysUserPostEntity userPostEntity = new SysUserPostEntity();
                userPostEntity.setPostId(o);
                userPostEntity.setUserId(userEntity.getUserId());
                return userPostEntity;
            }).toList());
        }
        if (delPostIds.size() > 0) {
            sysUserPostDao.delete(new QueryWrapper<SysUserPostEntity>().eq("user_id", userEntity.getUserId()).in("post_id", delPostIds));
        }
        clearCache(request.getUserId());
        return ResponseData.success();
    }

    @Transactional
    @Override
    public ResponseData<SysUserSecretVO> resetSecret(Long userId) {
        SysUserEntity sysUserEntity = findById(userId);
        if (sysUserEntity == null) {
            return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale(), "user not found"));
        }
        SysUserEntity updateEntity = new SysUserEntity();
        updateEntity.setUserId(userId);
        String originPassword = RandomStringUtils.randomAlphanumeric(10);
        String oncePassword = HMacMD5Utils.hash(CodecData.utf8(originPassword), CodecData.utf8(originPassword)).hex().toLowerCase();
        updateEntity.setPassword(passwordEncoder.encode(oncePassword));
        String GoogleOTPKey = OTPUtils.generateSecretKey();
        updateEntity.setGoogleOTPKey(GoogleOTPKey);
        updateEntity.setUpdateBy(SessionContext.getSessionUserId());
        updateEntity.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(updateEntity);
        SysUserSecretVO sysUserSecretVO = new SysUserSecretVO();
        sysUserSecretVO.setPassword(Base64Utils.encode(originPassword.getBytes(StandardCharsets.UTF_8)));
        if (securityProperties.isMfa() && BooleanUtils.toBoolean(sysUserEntity.getEnableMFA())) {
            sysUserSecretVO.setOtpImgBase64(OTPUtils.generateQRCode(sysUserEntity.getUserName(), GoogleOTPKey));
        }
        clearCache(userId);
        return ResponseData.success(sysUserSecretVO);
    }

    @Transactional
    @Override
    public ResponseData authorize(SysUserAuthorizeRequest request) {
        List<Long> reqRoleIds = request.getRoleIds() == null ? List.of() : request.getRoleIds();
        // 普通管理员授权限制逻辑
        if (!SessionContext.isAdmin()) {
            // 普通用户不能授权超管角色给其他用户
            if (reqRoleIds.size() > 0) {
                if (sysRoleDao.selectList(new QueryWrapper<SysRoleEntity>().in("role_id", reqRoleIds)).parallelStream().anyMatch(p -> SecuritySupport.hasAdmin(p.getRolePerms())))
                    return ResponseData.fail(ErrorCode.build(4, Stream.of("普通管理员不能授权含超管权限的角色给其他用户").toArray()));
            }
            // 普通用户不能为含有超管权限的用户授权
            List<SysRoleEntity> userRoleList = sysRoleDao.queryUserRoleList(request.getUserId(), null);
            boolean userHasAdmin = userRoleList.parallelStream().anyMatch(r -> SecuritySupport.hasAdmin(r.getRolePerms()));
            if (userHasAdmin)
                return ResponseData.fail(ErrorCode.build(4, Stream.of("普通管理员不能为含有超管权限用户授权").toArray()));
        }

        List<Long> existsRoleIds = sysUserRoleDao.selectList(new QueryWrapper<SysUserRoleEntity>().eq("user_id", request.getUserId())).stream().map(p -> p.getRoleId()).toList();
        List<Long> addRoleIds = reqRoleIds.stream().filter(o -> !existsRoleIds.contains(o)).toList();
        List<Long> delRoleIds = existsRoleIds.stream().filter(o -> !reqRoleIds.contains(o)).toList();

        if (addRoleIds.size() > 0) {
            sysUserRoleDao.insert(addRoleIds.stream().map(o -> {
                SysUserRoleEntity userRoleEntity = new SysUserRoleEntity();
                userRoleEntity.setRoleId(o);
                userRoleEntity.setUserId(request.getUserId());
                return userRoleEntity;
            }).toList());
        }
        if (delRoleIds.size() > 0) {
            sysUserRoleDao.delete(new QueryWrapper<SysUserRoleEntity>().eq("user_id", request.getUserId()).in("role_id", delRoleIds));
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData delete(Long userId) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setUserId(userId);
        userEntity.setDeleted(1);
        userEntity.setUpdateBy(SessionContext.getSessionUserId());
        userEntity.setUpdateTime(LocalDateTime.now());
        int num = baseMapper.updateById(userEntity);
        if (num > 0) {
            return ResponseData.success();
        }
        return ResponseData.fail(ErrorCode.build(4, LocaleContextHolder.getLocale()));
    }

    public void clearCache(Long userId) {
        redisTemplate.delete(String.format(CACHE_USER_BY_USERID, userId));
        redisTemplate.delete(String.format(CACHE_USER_BY_USERNAME, findById(userId).getUserName()));
    }

}
