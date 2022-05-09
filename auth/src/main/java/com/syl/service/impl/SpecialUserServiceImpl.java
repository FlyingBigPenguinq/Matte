package com.syl.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.dto.SysUserDTO;
import com.syl.entity.LoginUser;
import com.syl.entity.SysUser;
import com.syl.entity.UserInfo;
import com.syl.exception.RunException;
import com.syl.mapper.SysMenuMapper;
import com.syl.mapper.SysRoleMapper;
import com.syl.mapper.SysUserMapper;
import com.syl.service.IAuthUserService;
import com.syl.service.SpecialUserService;
import com.syl.util.JwtUtil;
import com.syl.util.RedisUtil;
import com.syl.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @author Liu XiangLiang
 * @description: sda
 * @date 2022/5/2 下午1:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SpecialUserService {

    @Autowired
    private final SysUserMapper sysUserMapper;

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Autowired
    private final AuthenticationManager authenticationManager;

    private final SysMenuMapper sysMenuMapper;

    private final SysRoleMapper sysRoleMapper;

    @Override
    public Map<Object, Object> specialLogin(SysUser sysUser) throws Exception {

        SysUserDTO sysUserDTO = sysUserMapper.getInfoByUsername(sysUser.getUsername());
        if (Boolean.TRUE.equals(sysUserDTO.getStatus())) {
            throw new RunException("this is a ban user");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(sysUser.getUsername(), "123456");
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
