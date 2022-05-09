package com.syl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.entity.SysUser;
import com.syl.entity.UserInfo;

import java.util.Map;

/**
 * @author Liu XiangLiang
 */
public interface IAuthUserService extends IService<SysUser> {
    /**
     * 登录
     * @param sysUser 用户信息
     * @return 用户信息 + 权限信息
     * @throws Exception 异常
     */
    Map<Object, Object> login(SysUser sysUser) throws Exception;

    /**
     * 登出
     */
    void logout();

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserInfo getUserInfo(String username);
}
