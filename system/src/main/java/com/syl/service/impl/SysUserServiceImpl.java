package com.syl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.dto.SysUserDTO;
import com.syl.dto.SysUserSaveOrUpdateDTO;
import com.syl.dto.SysUserSearchQueryDTO;
import com.syl.entity.SysUser;
import com.syl.enums.ConfigEnum;
import com.syl.excel.sheet.SysUserImportSheet;
import com.syl.excel.sheet.SysUserSheet;
import com.syl.exception.RunException;
import com.syl.mapper.SysRoleMapper;
import com.syl.mapper.SysUserMapper;
import com.syl.response.PageData;
import com.syl.service.ISysUserService;
import com.syl.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper sysUserMapper;

    private final SysRoleMapper sysRoleMapper;

    @Override
    public PageData<SysUserVO> listSysUser(SysUserSearchQueryDTO sysUserSearchQueryDTO, Long page, Long size) {
        // 断言判断参数 异常由全局处理
        notNull(sysUserSearchQueryDTO, page, size);
        // 判断用户模糊条件查询
        LambdaQueryWrapper<SysUser> wrapper = getSysUserLambdaQueryWrapper(sysUserSearchQueryDTO);
        // 转VO
        IPage<SysUserDTO> listPage = sysUserMapper.listPage(new Page<>(page, size), wrapper);
        List<SysUserVO> sysUserListVO = new ArrayList<>();
        for (SysUserDTO record : listPage.getRecords()) {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(record, sysUserVO);
            sysUserListVO.add(sysUserVO);
        }
        return new PageData<>(listPage.getCurrent(), listPage.getSize(), listPage.getTotal(), sysUserListVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSysUser(SysUserSaveOrUpdateDTO sysUserSaveOrUpdateDTO) {
        Assert.notNull(sysUserSaveOrUpdateDTO, "用户为null");
        // 判断username是否重复
        Integer count = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUserSaveOrUpdateDTO.getUsername()));
        if (count > 0) {
            throw new RunException("用户的账号重复");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserSaveOrUpdateDTO, sysUser);
        // 生成默认密码
        String password = new BCryptPasswordEncoder().encode(ConfigEnum.DEFAULT_PASSWORD.getValue());
        sysUser.setPassword(password);
        sysUserMapper.insert(sysUser);
        // 添加用户-角色关系
        sysUserMapper.insertUserRole(sysUser.getId(), sysUserSaveOrUpdateDTO.getRoles());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysUser(SysUserSaveOrUpdateDTO sysUserSaveOrUpdateDTO) {
        Assert.notNull(sysUserSaveOrUpdateDTO.getId(), "ID为空null");
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, sysUserSaveOrUpdateDTO.getUsername());
        wrapper.ne(SysUser::getId, sysUserSaveOrUpdateDTO.getId());
        int count = sysUserMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RunException("用户的账号重复");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserSaveOrUpdateDTO, sysUser);
        sysUserMapper.updateById(sysUser);
        // 修改用户-角色关系
        Set<Long> userId = new HashSet<>();
        userId.add(sysUser.getId());
        sysUserMapper.updateUserRole(sysUser.getId(), sysUserSaveOrUpdateDTO.getRoles());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSysUser(Set<Long> id) {
        Assert.notEmpty(id, "ID为空");
        sysUserMapper.deleteBatchIds(id);
        // 删除用户角色关联
        sysUserMapper.deleteUserRoleByUserId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id) {
        Assert.notNull(id, "ID为null");
        SysUser sysUser = sysUserMapper.selectById(id);
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getId, id);
        String password = new BCryptPasswordEncoder().encode(ConfigEnum.DEFAULT_PASSWORD.getValue());
        wrapper.set(SysUser::getPassword, password);
        sysUserMapper.update(sysUser, wrapper);
    }

    @Override
    public List<SysUserSheet> listSysUserSheetsByQuery(SysUserSearchQueryDTO sysUserSearchQueryDTO) {
        // 断言判断参数 异常由全局处理
        notNull(sysUserSearchQueryDTO, 0L, 0L);
        // 判断用户模糊条件查询
        LambdaQueryWrapper<SysUser> wrapper = getSysUserLambdaQueryWrapper(sysUserSearchQueryDTO);
        List<SysUser> sysUsers = sysUserMapper.selectList(wrapper);
        // 转换
        List<SysUserSheet> sysUserSheetList = new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            SysUserSheet sysUserSheet = new SysUserSheet();
            BeanUtils.copyProperties(sysUser, sysUserSheet);
            sysUserSheetList.add(sysUserSheet);
        }
        return sysUserSheetList;
    }

    @Override
    public List<SysUserImportSheet> getSysUserSheetTemplate() {
        SysUserImportSheet sysUserImportSheet = new SysUserImportSheet();
        sysUserImportSheet.setUsername("tom(不可重复)");
        sysUserImportSheet.setName("tom");
        sysUserImportSheet.setGender(0);
        sysUserImportSheet.setBirthday(new Date());
        sysUserImportSheet.setAddress("北京");
        sysUserImportSheet.setTel("123456789");
        sysUserImportSheet.setEmail("123@qq.com");
        List<SysUserImportSheet> sysUserImportSheets = new ArrayList<>();
        sysUserImportSheets.add(sysUserImportSheet);
        return sysUserImportSheets;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> importSysUser(MultipartFile excel) throws IOException {
        String fileName = excel.getOriginalFilename();
        // 把文件传到临时地方 便于读取
        String rootFilePath = System.getProperty("user.dir") + '\\' + fileName;
        FileUtil.writeBytes(excel.getBytes(), rootFilePath);
        // 校验成功的数据
        List<SysUser> sysUserList = new ArrayList<>();
        // 计算校验失败的数据
        AtomicInteger failNum = new AtomicInteger();
        // 判断文件类型
        if (CharSequenceUtil.endWithIgnoreCase(fileName, ".xlsx") || CharSequenceUtil.endWithIgnoreCase(fileName, ".xls")) {
            // 读取excel信息
            EasyExcelFactory.read(fileName, SysUserImportSheet.class, new PageReadListener<SysUserImportSheet>(dataList -> {
                for (SysUserImportSheet data : dataList) {
                    // 查重数据
                    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(!StrUtil.isBlankIfStr(data.getUsername()), SysUser::getUsername, data.getUsername());
                    Integer count = sysUserMapper.selectCount(wrapper);
                    if (count == 0) {
                        SysUser sysUser = new SysUser();
                        BeanUtils.copyProperties(data, sysUser);
                        sysUser.setPassword(new BCryptPasswordEncoder().encode(ConfigEnum.DEFAULT_PASSWORD.getValue()));
                        sysUserList.add(sysUser);
                    } else {
                        failNum.getAndIncrement();
                    }
                }
            })).sheet().doRead();
            FileUtil.del(rootFilePath);
            // 批量插入
            saveBatch(sysUserList);
        } else {
            FileUtil.del(rootFilePath);
            throw new RunException("文件格式不正确");
        }
        return MapUtil.builder()
                .put("success", sysUserList.size())
                .put("fail", failNum)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Boolean status) {
        Assert.notNull(id, "id不能为空");
        Assert.notNull(status, "状态不能为空");
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getId, id);
        wrapper.set(SysUser::getStatus, status);
        sysUserMapper.update(null, wrapper);
    }

    /**
     * 断言判断参数 异常由全局处理
     *
     * @param sysUserSearchQueryDTO 查询条件
     * @param page                  页码
     * @param size                  页数
     */
    private void notNull(SysUserSearchQueryDTO sysUserSearchQueryDTO, Long page, Long size) {
        Assert.notNull(page, "Page为null");
        Assert.notNull(size, "Size为null");
        Assert.notNull(sysUserSearchQueryDTO, "用户条件查询为null");
    }

    /**
     * 判断用户模糊条件查询
     *
     * @param sysUserSearchQueryDTO sysUserSearchQueryDTO
     * @return wrapper
     */
    private LambdaQueryWrapper<SysUser> getSysUserLambdaQueryWrapper(SysUserSearchQueryDTO sysUserSearchQueryDTO) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CharSequenceUtil.isNotBlank(sysUserSearchQueryDTO.getUsername()), SysUser::getUsername, sysUserSearchQueryDTO.getUsername());
        wrapper.like(CharSequenceUtil.isNotBlank(sysUserSearchQueryDTO.getName()), SysUser::getName, sysUserSearchQueryDTO.getName());
        wrapper.like(ObjectUtil.isNotNull(sysUserSearchQueryDTO.getGender()), SysUser::getGender, sysUserSearchQueryDTO.getGender());
        wrapper.like(CharSequenceUtil.isNotBlank(sysUserSearchQueryDTO.getTel()), SysUser::getTel, sysUserSearchQueryDTO.getTel());
        wrapper.like(CharSequenceUtil.isNotBlank(sysUserSearchQueryDTO.getAddress()), SysUser::getAddress, sysUserSearchQueryDTO.getAddress());
        wrapper.like(CharSequenceUtil.isNotBlank(sysUserSearchQueryDTO.getEmail()), SysUser::getEmail, sysUserSearchQueryDTO.getEmail());
        wrapper.eq(ObjectUtil.isNotNull(sysUserSearchQueryDTO.getStatus()), SysUser::getStatus, sysUserSearchQueryDTO.getStatus());
        if (CollUtil.isNotEmpty(sysUserSearchQueryDTO.getBirthday()) && ObjectUtil.isNotNull(sysUserSearchQueryDTO.getBirthday())) {
            wrapper.gt(SysUser::getBirthday, sysUserSearchQueryDTO.getBirthday().get(0));
            wrapper.le(SysUser::getBirthday, sysUserSearchQueryDTO.getBirthday().get(1));
        }
        return wrapper;
    }
}
