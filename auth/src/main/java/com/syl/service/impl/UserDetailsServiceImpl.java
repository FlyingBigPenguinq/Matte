package com.syl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.syl.dto.SysUserDTO;
import com.syl.entity.LoginUser;
import com.syl.entity.SysUser;
import com.syl.exception.LoginUserAuthenticationException;
import com.syl.mapper.SysMenuMapper;
import com.syl.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Liu XiangLiang
 * @description 重写security用户接口
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserMapper sysUserMapper;
    private final SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username,"username为空");
        // 查询用户
        SysUserDTO sysUser = sysUserMapper.getInfoByUsername(username);
        if (ObjectUtil.isNull(sysUser)) {
            log.error("用户名或密码错误");
            throw new LoginUserAuthenticationException("用户名或密码错误");
        }
        // 查询权限
        List<String> permission = sysMenuMapper.listPermissionByUserId(sysUser.getId());
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUser, user);
        return new LoginUser(user, permission);
    }
}
