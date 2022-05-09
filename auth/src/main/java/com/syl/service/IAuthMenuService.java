package com.syl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.entity.SysMenu;
import com.syl.vo.SysMenuVO;

import java.util.List;

/**
 * @author Liu XiangLiang
 */

public interface IAuthMenuService extends IService<SysMenu> {
    /**
     * 获取当前用户的菜单
     * @param username /
     * @return /
     */
    List<SysMenuVO> getMenuByCurrentUser(String username);
}
