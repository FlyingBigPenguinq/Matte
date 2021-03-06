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
        // ?????????????????? ?????????????????????
        notNull(sysUserSearchQueryDTO, page, size);
        // ??????????????????????????????
        LambdaQueryWrapper<SysUser> wrapper = getSysUserLambdaQueryWrapper(sysUserSearchQueryDTO);
        // ???VO
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
        Assert.notNull(sysUserSaveOrUpdateDTO, "?????????null");
        // ??????username????????????
        Integer count = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUserSaveOrUpdateDTO.getUsername()));
        if (count > 0) {
            throw new RunException("?????????????????????");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserSaveOrUpdateDTO, sysUser);
        // ??????????????????
        String password = new BCryptPasswordEncoder().encode(ConfigEnum.DEFAULT_PASSWORD.getValue());
        sysUser.setPassword(password);
        sysUserMapper.insert(sysUser);
        // ????????????-????????????
        sysUserMapper.insertUserRole(sysUser.getId(), sysUserSaveOrUpdateDTO.getRoles());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysUser(SysUserSaveOrUpdateDTO sysUserSaveOrUpdateDTO) {
        Assert.notNull(sysUserSaveOrUpdateDTO.getId(), "ID??????null");
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, sysUserSaveOrUpdateDTO.getUsername());
        wrapper.ne(SysUser::getId, sysUserSaveOrUpdateDTO.getId());
        int count = sysUserMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RunException("?????????????????????");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserSaveOrUpdateDTO, sysUser);
        sysUserMapper.updateById(sysUser);
        // ????????????-????????????
        Set<Long> userId = new HashSet<>();
        userId.add(sysUser.getId());
        sysUserMapper.updateUserRole(sysUser.getId(), sysUserSaveOrUpdateDTO.getRoles());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSysUser(Set<Long> id) {
        Assert.notEmpty(id, "ID??????");
        sysUserMapper.deleteBatchIds(id);
        // ????????????????????????
        sysUserMapper.deleteUserRoleByUserId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id) {
        Assert.notNull(id, "ID???null");
        SysUser sysUser = sysUserMapper.selectById(id);
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getId, id);
        String password = new BCryptPasswordEncoder().encode(ConfigEnum.DEFAULT_PASSWORD.getValue());
        wrapper.set(SysUser::getPassword, password);
        sysUserMapper.update(sysUser, wrapper);
    }

    @Override
    public List<SysUserSheet> listSysUserSheetsByQuery(SysUserSearchQueryDTO sysUserSearchQueryDTO) {
        // ?????????????????? ?????????????????????
        notNull(sysUserSearchQueryDTO, 0L, 0L);
        // ??????????????????????????????
        LambdaQueryWrapper<SysUser> wrapper = getSysUserLambdaQueryWrapper(sysUserSearchQueryDTO);
        List<SysUser> sysUsers = sysUserMapper.selectList(wrapper);
        // ??????
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
        sysUserImportSheet.setUsername("tom(????????????)");
        sysUserImportSheet.setName("tom");
        sysUserImportSheet.setGender(0);
        sysUserImportSheet.setBirthday(new Date());
        sysUserImportSheet.setAddress("??????");
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
        // ??????????????????????????? ????????????
        String rootFilePath = System.getProperty("user.dir") + '\\' + fileName;
        FileUtil.writeBytes(excel.getBytes(), rootFilePath);
        // ?????????????????????
        List<SysUser> sysUserList = new ArrayList<>();
        // ???????????????????????????
        AtomicInteger failNum = new AtomicInteger();
        // ??????????????????
        if (CharSequenceUtil.endWithIgnoreCase(fileName, ".xlsx") || CharSequenceUtil.endWithIgnoreCase(fileName, ".xls")) {
            // ??????excel??????
            EasyExcelFactory.read(fileName, SysUserImportSheet.class, new PageReadListener<SysUserImportSheet>(dataList -> {
                for (SysUserImportSheet data : dataList) {
                    // ????????????
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
            // ????????????
            saveBatch(sysUserList);
        } else {
            FileUtil.del(rootFilePath);
            throw new RunException("?????????????????????");
        }
        return MapUtil.builder()
                .put("success", sysUserList.size())
                .put("fail", failNum)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Boolean status) {
        Assert.notNull(id, "id????????????");
        Assert.notNull(status, "??????????????????");
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getId, id);
        wrapper.set(SysUser::getStatus, status);
        sysUserMapper.update(null, wrapper);
    }

    /**
     * ?????????????????? ?????????????????????
     *
     * @param sysUserSearchQueryDTO ????????????
     * @param page                  ??????
     * @param size                  ??????
     */
    private void notNull(SysUserSearchQueryDTO sysUserSearchQueryDTO, Long page, Long size) {
        Assert.notNull(page, "Page???null");
        Assert.notNull(size, "Size???null");
        Assert.notNull(sysUserSearchQueryDTO, "?????????????????????null");
    }

    /**
     * ??????????????????????????????
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
