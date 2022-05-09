package com.syl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.dto.SysUserDTO;
import com.syl.entity.SysMenu;
import com.syl.mapper.SysMenuMapper;
import com.syl.mapper.SysUserMapper;
import com.syl.service.IAuthMenuService;
import com.syl.vo.SysMenuMetaVo;
import com.syl.vo.SysMenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liu XiangLiang
 * @description 菜单service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements IAuthMenuService {

    private final SysMenuMapper sysMenuMapper;

    private final SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuVO> getMenuByCurrentUser(String username) {
        Assert.notBlank(username, "当前用户名为空");
        SysUserDTO sysUser = sysUserMapper.getInfoByUsername(username);
        List<SysMenu> sysMenus = sysMenuMapper.listMenuByUserId(sysUser.getId());
        if (sysMenus.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<SysMenuVO> sysMenuListVO = toSysMenuVO(sysMenus);
            //转树状
            List<SysMenu> menus = buildTreeMenu(sysMenus);
            //转VO
            return toSysMenuVO(menus);
        }
    }

    private List<SysMenuVO> toSysMenuVO(List<SysMenu> menuList){
        ArrayList<SysMenuVO> sysMenuListVO = new ArrayList<>();
        for (SysMenu sysMenu : menuList) {
            SysMenuVO sysMenuVO = new SysMenuVO();
            sysMenuVO.setName(sysMenu.getTitle());
            sysMenuVO.setPath(sysMenu.getPath());
            sysMenuVO.setHidden(sysMenu.getHidden());
            if (StringUtils.isEmpty(sysMenu.getComponent())){
                sysMenuVO.setComponent("Layout");
            }else {
                sysMenuVO.setComponent(sysMenu.getComponent());
            }

            SysMenuMetaVo sysMenuMetaVo = new SysMenuMetaVo();
            sysMenuMetaVo.setTitle(sysMenu.getTitle());
            sysMenuMetaVo.setIcon(sysMenu.getIcon());
            sysMenuVO.setMeta(sysMenuMetaVo);
            if (CollUtil.isNotEmpty(sysMenu.getChildren())){
                sysMenuVO.setChildren(toSysMenuVO(sysMenu.getChildren()));
            }
            sysMenuListVO.add(sysMenuVO);
        }
        return sysMenuListVO;
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
        //根据根节点递归找子节点
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
        if (CollUtil.isNotEmpty(menuList)){
            sysMenu.setChildren(menuList);
            for (SysMenu menu : menuList) {
                selectNodeMenu(allMenus,menu);
            }
        }
    }
}
