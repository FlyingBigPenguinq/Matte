package com.syl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.dto.SysRoleSaveOrUpdateDTO;
import com.syl.dto.SysRoleSearchQueryDTO;
import com.syl.dto.SysRoleUserSearchQueryDTO;
import com.syl.entity.SysRole;
import com.syl.entity.SysUser;
import com.syl.exception.RunException;
import com.syl.mapper.SysRoleMapper;
import com.syl.mapper.SysUserMapper;
import com.syl.response.PageData;
import com.syl.service.ISysRoleService;
import com.syl.vo.SysRoleVO;
import com.syl.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    private final SysRoleMapper sysRoleMapper;

    private final SysUserMapper sysUserMapper;

    @Override
    public PageData<SysRoleVO> listSysRole(SysRoleSearchQueryDTO sysRoleSearchQueryDTO, Long page, Long size) {
        // 断言判空
        notNullToListSysRole(sysRoleSearchQueryDTO, page, size);
        // 查询
        LambdaQueryWrapper<SysRole> wrapper = getSysRoleLambdaQueryWrapper(sysRoleSearchQueryDTO);
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(new Page<>(page, size), wrapper);
        // 转VO
        List<SysRoleVO> sysRoleListVO = toVO(sysRolePage.getRecords());
        return new PageData<>(sysRolePage.getCurrent(), sysRolePage.getSize(), sysRolePage.getTotal(), sysRoleListVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSysRole(SysRoleSaveOrUpdateDTO sysRoleSaveOrUpdateDTO) {
        Assert.notNull(sysRoleSaveOrUpdateDTO, "角色为null");
        //查重名字和标识
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getName, sysRoleSaveOrUpdateDTO.getName());
        wrapper.or().eq(SysRole::getCode, sysRoleSaveOrUpdateDTO.getCode());
        Integer count = sysRoleMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RunException("角色名称或者标识已存在");
        }
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleSaveOrUpdateDTO, sysRole);
        sysRoleMapper.insert(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysRole(SysRoleSaveOrUpdateDTO sysRoleSaveOrUpdateDTO) {
        Assert.notNull(sysRoleSaveOrUpdateDTO, "角色为null");
        //查重名字和标识
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(SysRole::getId, sysRoleSaveOrUpdateDTO.getId());
        wrapper.and(i -> i.eq(SysRole::getName, sysRoleSaveOrUpdateDTO.getName()).or().eq(SysRole::getCode, sysRoleSaveOrUpdateDTO.getCode()));
        Integer count = sysRoleMapper.selectCount(wrapper);
        SysRole sysRole1 = sysRoleMapper.selectOne(wrapper);
        if (count > 0) {
            throw new RunException("角色名称或者标识已存在");
        }
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleSaveOrUpdateDTO, sysRole);
        sysRoleMapper.updateById(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSysUser(Set<Long> id) {
        Assert.notNull(id, "id为null");
        sysRoleMapper.deleteBatchIds(id);
        // 删除角色与菜单的关联
        sysRoleMapper.deleteRoleMenuByRoleId(id);
    }

    @Override
    public PageData<SysUserVO> listAuthSysUserByRoleIdAndPageQuery(SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO, Long id, Long page, Long size) {
        // 断言判空
        notNullToListSysUserByRoleIdAndPageQuery(sysRoleUserSearchQueryDTO, id, page, size);
        // 查询关联表获取用户id
        List<Long> userIds = sysRoleMapper.listUserIdByRoleId(id);
        if (userIds.isEmpty()) {
            return new PageData<>();
        }
        // 根据用户id和查询条件查询用户
        return getSysUserVOPageData(sysRoleUserSearchQueryDTO, page, size, userIds);
    }

    @Override
    public PageData<SysUserVO> listUnAuthSysUserByRoleIdAndPageQuery(SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO, Long id, Long page, Long size) {
        // 断言判空
        notNullToListSysUserByRoleIdAndPageQuery(sysRoleUserSearchQueryDTO, id, page, size);
        // 查询关联表获取没有授权的用户id
        List<Long> userIds = sysRoleMapper.listUnAuthUserIdByRoleId(id);
        if (userIds.isEmpty()) {
            return new PageData<>();
        }
        // 根据用户id和查询条件查询用户
        return getSysUserVOPageData(sysRoleUserSearchQueryDTO, page, size, userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authSysUser(Set<Long> ids, Long roleId) {
        Assert.notNull(ids, "ids为null");
        Assert.notNull(roleId, "roleId为null");
        // 插入角色与用户的关联
        sysRoleMapper.insertRoleUser(ids, roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unAuthSysUser(Set<Long> userIds, Long roleId) {
        Assert.notNull(userIds, "userIds为null");
        Assert.notNull(roleId, "roleId为null");
        // 删除角色与用户的关联
        sysRoleMapper.deleteRoleUser(userIds, roleId);
    }

    @Override
    public List<SysRoleVO> getSysRole() {
        List<SysRole> sysRoleList = list();
        return toVO(sysRoleList);
    }

    @Override
    public List<Long> getSysMenu(Long id) {
        Assert.notNull(id, "id为null");
        return sysRoleMapper.getSysMenu(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSysMenu(Long id, Set<Long> menuIds) {
        Assert.notNull(id, "角色id为null");
        Assert.notNull(menuIds, "菜单id为null");
        sysRoleMapper.saveSysMenu(id, menuIds);
    }

    /**
     * SysRole转SysRoleVO
     *
     * @param sysRoleList /
     * @return /
     */
    private List<SysRoleVO> toVO(List<SysRole> sysRoleList) {
        List<SysRoleVO> sysRoleListVO = new ArrayList<>();
        for (SysRole record : sysRoleList) {
            SysRoleVO sysRoleVO = new SysRoleVO();
            BeanUtils.copyProperties(record, sysRoleVO);
            sysRoleListVO.add(sysRoleVO);
        }
        return sysRoleListVO;
    }

    /**
     * 根据用户id和查询条件查询用户
     *
     * @param sysRoleUserSearchQueryDTO 查询条件
     * @param page                      当前页
     * @param size                      每页大小
     * @param userIds                   用户id
     * @return 用户分页数据
     */
    private PageData<SysUserVO> getSysUserVOPageData(SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO, Long page, Long size, List<Long> userIds) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CharSequenceUtil.isNotBlank(sysRoleUserSearchQueryDTO.getName()), SysUser::getName, sysRoleUserSearchQueryDTO.getName());
        wrapper.like(CharSequenceUtil.isNotBlank(sysRoleUserSearchQueryDTO.getUsername()), SysUser::getUsername, sysRoleUserSearchQueryDTO.getUsername());
        wrapper.in(CollUtil.isNotEmpty(userIds), SysUser::getId, userIds);
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(new Page<>(page, size), wrapper);
        List<SysUserVO> sysUserListVO = new ArrayList<>();
        for (SysUser record : sysUserPage.getRecords()) {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(record, sysUserVO);
            sysUserListVO.add(sysUserVO);
        }
        return new PageData<>(sysUserPage.getCurrent(), sysUserPage.getSize(), sysUserPage.getTotal(), sysUserListVO);
    }

    /**
     * 角色模糊查询
     *
     * @param sysRoleSearchQueryDTO 条件
     * @return /
     */
    private LambdaQueryWrapper<SysRole> getSysRoleLambdaQueryWrapper(SysRoleSearchQueryDTO sysRoleSearchQueryDTO) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CharSequenceUtil.isNotBlank(sysRoleSearchQueryDTO.getName()), SysRole::getName, sysRoleSearchQueryDTO.getName());
        wrapper.like(CharSequenceUtil.isNotBlank(sysRoleSearchQueryDTO.getCode()), SysRole::getCode, sysRoleSearchQueryDTO.getCode());
        return wrapper;
    }

    /**
     * 断言判空
     *
     * @param sysRoleSearchQueryDTO 角色条件查询
     * @param page                  页数
     * @param size                  页码
     */
    private void notNullToListSysRole(SysRoleSearchQueryDTO sysRoleSearchQueryDTO, Long page, Long size) {
        Assert.notNull(sysRoleSearchQueryDTO, "角色条件查询值为null");
        Assert.notNull(page, "页码为null");
        Assert.notNull(size, "页数为null");
    }

    /**
     * 断言判空
     *
     * @param sysRoleUserSearchQueryDTO 角色条件查询
     * @param id                        角色id
     * @param page                      页数
     * @param size                      页码
     */
    private void notNullToListSysUserByRoleIdAndPageQuery(SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO, Long id, Long page, Long size) {
        Assert.notNull(sysRoleUserSearchQueryDTO, "角色授权用户查询为null");
        Assert.notNull(id, "角色id为null");
        Assert.notNull(page, "页码为null");
        Assert.notNull(size, "页大小为null");
    }
}
