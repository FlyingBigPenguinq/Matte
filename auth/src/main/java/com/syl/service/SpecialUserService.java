package com.syl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.entity.SysUser;

import java.util.Map;

/**
 * @author Liu XiangLiang
 * @description:
 * @date 2022/5/2 下午1:42
 */
public interface SpecialUserService extends IService<SysUser> {

    Map<Object, Object> specialLogin(SysUser sysUser) throws Exception;
}
