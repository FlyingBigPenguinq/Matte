package com.syl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syl.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 * @description 菜单mapper
 */
@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据userID查询全部权限
     * @param userId /
     * @return /
     */
    List<String> listPermissionByUserId(Long userId);

    /**
     * 根据userId查询全部菜单
     * @param userId /
     * @return /
     */
    List<SysMenu> listMenuByUserId(Long userId);

    /**
     * 删除菜单与角色的关联
     * @param id 菜单id
     */
    void deleteMenuRoleByMenuId(Long id);
}
