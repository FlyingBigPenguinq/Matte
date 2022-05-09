package com.syl.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.syl.dto.SysUserDTO;
import com.syl.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
@Repository
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 删除用户角色关系
     * @param id 用户id
     */
    void deleteUserRoleByUserId(Set<Long> id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    SysUserDTO getInfoByUsername(String username);

    /**
     * 用户-角色关系分页查询
     * @param page 分页对象
     * @param ew 查询条件
     * @return 用户-角色数据集合
     */
    IPage<SysUserDTO> listPage(IPage<?> page, Wrapper ew);

    /**
     * 插入用户-角色关系
     * @param userId 用户id
     * @param roles 角色id集合
     */
    void insertUserRole(Long userId, Set<Long> roles);

    /**
     * 更新用户-角色关系
     * @param userId 用户id
     * @param roles 角色id集合
     */
    void updateUserRole(Long userId, Set<Long> roles);
}
