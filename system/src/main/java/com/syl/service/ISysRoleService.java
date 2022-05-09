package com.syl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.dto.SysRoleSaveOrUpdateDTO;
import com.syl.dto.SysRoleSearchQueryDTO;
import com.syl.dto.SysRoleUserSearchQueryDTO;
import com.syl.entity.SysRole;
import com.syl.response.PageData;
import com.syl.vo.SysRoleVO;
import com.syl.vo.SysUserVO;

import java.util.List;
import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */

public interface ISysRoleService extends IService<SysRole> {
    /**
     * 获取分页条件查询
     *
     * @param sysRoleSearchQueryDTO 条件
     * @param page                  页码
     * @param size                  页数
     * @return PageData<SysRoleVO>
     */
    PageData<SysRoleVO> listSysRole(SysRoleSearchQueryDTO sysRoleSearchQueryDTO, Long page, Long size);

    /**
     * 添加系统角色
     *
     * @param sysRoleSaveOrUpdateDTO 系统角色DTO
     */
    void saveSysRole(SysRoleSaveOrUpdateDTO sysRoleSaveOrUpdateDTO);

    /**
     * 修改系统角色
     *
     * @param sysRoleSaveOrUpdateDTO 系统角色DTO
     */
    void updateSysRole(SysRoleSaveOrUpdateDTO sysRoleSaveOrUpdateDTO);

    /**
     * 删除系统角色
     *
     * @param id 系统角色id
     */
    void delSysUser(Set<Long> id);

    /**
     * 获取授权用户信息
     *
     * @param sysRoleUserSearchQueryDTO 查询条件
     * @param id                        系统角色id
     * @param page                      页码
     * @param size                      页数
     * @return PageData<SysUserVO>
     */
    PageData<SysUserVO> listAuthSysUserByRoleIdAndPageQuery(SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO, Long id, Long page, Long size);

    /**
     * 获取未授权用户信息
     *
     * @param sysRoleUserSearchQueryDTO 查询条件
     * @param id                        系统角色id
     * @param page                      页码
     * @param size                      页数
     * @return PageData<SysUserVO>
     */
    PageData<SysUserVO> listUnAuthSysUserByRoleIdAndPageQuery(SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO, Long id, Long page, Long size);

    /**
     * 授权用户
     *
     * @param userIds 用户id集合
     * @param roleId  系统角色id
     */
    void authSysUser(Set<Long> userIds, Long roleId);

    /**
     * 取消授权用户
     *
     * @param userIds 用户id集合
     * @param roleId  系统角色id
     */
    void unAuthSysUser(Set<Long> userIds, Long roleId);

    /**
     * 获取系统角色列表
     *
     * @return List<SysRole>
     */
    List<SysRoleVO> getSysRole();

    /**
     * 根据角色ID获取系统菜单id
     *
     * @param id 系统角色id
     * @return Set<Long>
     */
    List<Long> getSysMenu(Long id);

    /**
     * 根据角色ID添加系统菜单id
     *
     * @param id      系统角色id
     * @param menuIds 系统菜单id集合
     */
    void saveSysMenu(Long id, Set<Long> menuIds);
}
