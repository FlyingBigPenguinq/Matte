package com.syl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.dto.SysMenuSaveOrUpdateDTO;
import com.syl.dto.SysMenuSearchQueryDTO;
import com.syl.entity.SysMenu;
import com.syl.exception.RunException;
import com.syl.mapper.SysMenuMapper;
import com.syl.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listSysMenu(SysMenuSearchQueryDTO sysMenuSearchQueryDTO) {
        Assert.notNull(sysMenuSearchQueryDTO, "菜单查询参数不能为空");
        // 查询
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CharSequenceUtil.isNotBlank(sysMenuSearchQueryDTO.getTitle()), SysMenu::getTitle, sysMenuSearchQueryDTO.getTitle());
        wrapper.eq(ObjectUtil.isNotNull(sysMenuSearchQueryDTO.getHidden()), SysMenu::getHidden, sysMenuSearchQueryDTO.getHidden());
        List<SysMenu> sysMenus = sysMenuMapper.selectList(wrapper);
        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSysMenu(SysMenuSaveOrUpdateDTO sysMenuSaveOrUpdateDTO) {
        Assert.notNull(sysMenuSaveOrUpdateDTO, "菜单保存参数不能为空");
        Assert.notNull(sysMenuSaveOrUpdateDTO.getTitle(), "菜单标题不能为空");
        // 查重
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CharSequenceUtil.isNotBlank(sysMenuSaveOrUpdateDTO.getTitle()), SysMenu::getTitle, sysMenuSaveOrUpdateDTO.getTitle());
        Integer count = sysMenuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RunException("菜单标题已存在");
        }
        // 新增
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(sysMenuSaveOrUpdateDTO, sysMenu);
        sysMenuMapper.insert(sysMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysMenu(SysMenuSaveOrUpdateDTO sysMenuSaveOrUpdateDTO) {
        Assert.notNull(sysMenuSaveOrUpdateDTO, "菜单保存参数不能为空");
        Assert.notNull(sysMenuSaveOrUpdateDTO.getId(), "菜单id不能为空");
        Assert.notNull(sysMenuSaveOrUpdateDTO.getTitle(), "菜单标题不能为空");
        // 查重
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CharSequenceUtil.isNotBlank(sysMenuSaveOrUpdateDTO.getTitle()), SysMenu::getTitle, sysMenuSaveOrUpdateDTO.getTitle());
        wrapper.ne(ObjectUtil.isNotNull(sysMenuSaveOrUpdateDTO.getId()), SysMenu::getId, sysMenuSaveOrUpdateDTO.getId());
        Integer count = sysMenuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RunException("菜单标题已存在");
        }
        // 更新
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(sysMenuSaveOrUpdateDTO, sysMenu);
        sysMenuMapper.updateById(sysMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysMenu(Long id) {
        Assert.notNull(id, "菜单id不能为空");
        // 判断是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getPid, id);
        Integer count = sysMenuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RunException("请先删除子菜单");
        }
        // 删除
        sysMenuMapper.deleteById(id);
        // 删除菜单和角色的关联
        sysMenuMapper.deleteMenuRoleByMenuId(id);

    }

    @Override
    public List<SysMenu> listSysMenuToSelect(Long id) {
        Assert.notNull(id, "菜单id不能为空");
        // 查询
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(ObjectUtil.isNotNull(id), SysMenu::getId, id);
        wrapper.ne(ObjectUtil.isNotNull(id), SysMenu::getPid, id);
        List<SysMenu> sysMenus = sysMenuMapper.selectList(wrapper);
        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

    @Override
    public List<SysMenu> listSysMenuAllToSelect() {
        List<SysMenu> sysMenus = list();
        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> allMenus) {
        // 根节点集合
        List<SysMenu> sysMenuList = new ArrayList<>();
        // 遍历所有节点
        for (SysMenu sysMenu : allMenus) {
            // 没父节点，直接找到根节点
            if (sysMenu.getPid() == 0L) {
                sysMenuList.add(sysMenu);
                continue;
            }
            // 有父节点的，但是要判断是不是在allMenus中属不属于根节点
            int flag = 0;
            for (SysMenu menu : allMenus) {
                if (!menu.getId().equals(sysMenu.getPid())) {
                    flag++;
                }
                if (flag == allMenus.size()) {
                    sysMenuList.add(sysMenu);
                }
            }
        }
        // 根据根节点递归找子节点
        for (SysMenu sysMenu : sysMenuList) {
            selectNodeMenu(allMenus, sysMenu);
        }
        return sysMenuList;
    }

    private void selectNodeMenu(List<SysMenu> allMenus, SysMenu sysMenu) {
        allMenus.remove(sysMenu);
        List<SysMenu> menuList = allMenus.stream()
                .filter(menu -> sysMenu.getId().equals(menu.getPid()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(menuList)) {
            sysMenu.setChildren(menuList);
            for (SysMenu menu : menuList) {
                selectNodeMenu(allMenus, menu);
            }
        }
    }
}
