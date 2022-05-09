package com.syl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syl.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
@Repository
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 删除角色与菜单关系
     *
     * @param id 角色ID
     */
    void deleteRoleMenuByRoleId(Set<Long> id);

    /**
     * 获取角色id
     *
     * @param id 角色ID
     * @return 用户id
     */
    List<Long> listUserIdByRoleId(Long id);

    /**
     * 插入角色与用户关系
     *
     * @param userIds    用户ID
     * @param roleId 角色ID
     */
    void insertRoleUser(Set<Long> userIds, Long roleId);

    /**
     * 删除角色与用户关系
     * @param userIds 用户ID
     * @param roleId 角色ID
     */
    void deleteRoleUser(Set<Long> userIds, Long roleId);

    /**
     * 获取没有授权用户的用户id
     * @param roleId 角色ID
     * @return 用户id
     */
    List<Long> listUnAuthUserIdByRoleId(Long roleId);

    /**
     * 根据角色ID获取系统菜单id
     * @param id 角色ID
     * @return 系统菜单id
     */
    List<Long> getSysMenu(Long id);

    /**
     * 插入角色与系统菜单关系
     * @param roleId 角色ID
     * @param menuIds 系统菜单ID
     */
    void saveSysMenu(Long roleId, Set<Long> menuIds);

    /**
     * 根据用户id获取角色唯一标识
     * @param userId 用户id
     * @return 角色唯一标识
     */
    List<String> getRoleCodeByUserId(Long userId);
}
