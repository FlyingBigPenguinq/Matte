package com.syl.service.impl;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.config.RsaProperties;
import com.syl.dto.SysUserDTO;
import com.syl.entity.LoginUser;
import com.syl.entity.SysUser;
import com.syl.entity.UserInfo;
import com.syl.exception.RunException;
import com.syl.mapper.SysMenuMapper;
import com.syl.mapper.SysRoleMapper;
import com.syl.mapper.SysUserMapper;
import com.syl.service.IAuthUserService;
import com.syl.util.JwtUtil;
import com.syl.util.RedisUtil;
import com.syl.util.RsaUtils;
import com.syl.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @author Liu XiangLiang
 */
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IAuthUserService {

    private final AuthenticationManager authenticationManager;

    private final RedisUtil redisUtil;

    private final JwtUtil jwtUtil;

    private final SysUserMapper sysUserMapper;

    private final SysMenuMapper sysMenuMapper;

    private final SysRoleMapper sysRoleMapper;

    @Override
    public Map<Object, Object> login(SysUser sysUser) {
        Assert.notNull(sysUser, "sysUser为空");
        String password;
        // 密码解密
        try {
            password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, sysUser.getPassword());
        } catch (Exception e) {
            throw new RunException(e.getLocalizedMessage());
        }
        Assert.notNull(sysUser, "登录用户为空");
        // 查看用户的状态
        SysUserDTO user = sysUserMapper.getInfoByUsername(sysUser.getUsername());
        if (Boolean.TRUE.equals(user.getStatus())){
            throw new RunException("用户已被禁用");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(sysUser.getUsername(), password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getSysUser().getId().toString();
        String jwt = jwtUtil.generateToken(userId);
        redisUtil.set("login:" + userId, loginUser);
        // 获取用户信息
        UserInfo userInfo = getUserInfo(loginUser.getUsername());
        return MapUtil.builder()
                .put("token", jwt)
                .put("user", userInfo.getUser())
                .put("roles", userInfo.getRoles())
                .put("permissions", userInfo.getPermissions())
                .build();
    }

    @Override
    public void logout() {
        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) token.getPrincipal();
        Long userId = loginUser.getSysUser().getId();
        redisUtil.del("login:" + userId);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication((Authentication) null);
        SecurityContextHolder.clearContext();
    }

    @Override
    public UserInfo getUserInfo(String username) {
        Assert.notNull(username, "账号为空");
        SysUserDTO user = sysUserMapper.getInfoByUsername(username);
        SysUserVO userVO = new SysUserVO();
        BeanUtils.copyProperties(user, userVO);
        List<String> permission = sysMenuMapper.listPermissionByUserId(userVO.getId());
        List<String> roles = sysRoleMapper.getRoleCodeByUserId(userVO.getId());
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(userVO);
        userInfo.setPermissions(permission);
        userInfo.setRoles(roles);
        return userInfo;
    }


}
