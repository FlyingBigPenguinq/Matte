package com.syl.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.dto.SysMenuSaveOrUpdateDTO;
import com.syl.dto.SysMenuSearchQueryDTO;
import com.syl.entity.SysMenu;

import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 根据用户id查询菜单
     * @param sysMenuSearchQueryDTO 查询条件
     * @return 菜单列表
     */
    List<SysMenu> listSysMenu(SysMenuSearchQueryDTO sysMenuSearchQueryDTO);

    /**
     * 新增菜单
     * @param sysMenuSaveOrUpdateDTO 菜单信息
     */
    void saveSysMenu(SysMenuSaveOrUpdateDTO sysMenuSaveOrUpdateDTO);

    /**
     * 更新菜单
     * @param sysMenuSaveOrUpdateDTO 菜单信息
     */
    void updateSysMenu(SysMenuSaveOrUpdateDTO sysMenuSaveOrUpdateDTO);

    /**
     * 删除菜单
     * @param id 菜单id
     */
    void deleteSysMenu(Long id);

    /**
     * 根据菜单id查询下拉菜单
     * @param id 菜单id
     * @return 下拉菜单
     */
    List<SysMenu> listSysMenuToSelect(Long id);

    /**
     * 查询所有菜单
     * @return 菜单列表
     */
    List<SysMenu> listSysMenuAllToSelect();
}
