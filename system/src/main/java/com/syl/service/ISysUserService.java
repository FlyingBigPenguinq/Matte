package com.syl.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.dto.SysUserSaveOrUpdateDTO;
import com.syl.dto.SysUserSearchQueryDTO;
import com.syl.entity.SysUser;
import com.syl.excel.sheet.SysUserImportSheet;
import com.syl.excel.sheet.SysUserSheet;
import com.syl.response.PageData;
import com.syl.vo.SysUserVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
public interface ISysUserService extends IService<SysUser> {


    /**
     * 用户分页条件查询
     *
     * @param sysUserSearchQueryDTO 条件
     * @param page                  第几页
     * @param size                  多少页
     * @return /
     */
    PageData<SysUserVO> listSysUser(SysUserSearchQueryDTO sysUserSearchQueryDTO, Long page, Long size);

    /**
     * 添加用户
     *
     * @param sysUserSaveOrUpdateDTO 用户DTO
     */
    void saveSysUser(SysUserSaveOrUpdateDTO sysUserSaveOrUpdateDTO);

    /**
     * 更新用户
     *
     * @param sysUserSaveOrUpdateDTO 用户DTO
     */
    void updateSysUser(SysUserSaveOrUpdateDTO sysUserSaveOrUpdateDTO);

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    void delSysUser(Set<Long> id);

    /**
     * 重置用户密码
     *
     * @param id 用户id
     */
    void resetPassword(Long id);

    /**
     * 根据查询条件获取Excel格式的用户信息
     *
     * @param sysUserSearchQueryDTO 查询条件
     * @return Excel格式的用户信息
     */
    List<SysUserSheet> listSysUserSheetsByQuery(SysUserSearchQueryDTO sysUserSearchQueryDTO);

    /**
     * 获取Excel格式的用户信息模板
     *
     * @return Excel格式的用户信息模板
     */
    List<SysUserImportSheet> getSysUserSheetTemplate();

    /**
     * 导入用户信息
     *
     * @param excel 用户信息
     * @return 导入成功后的相关信息
     * @throws IOException 文件读取异常
     */
    Map<Object, Object> importSysUser(MultipartFile excel) throws IOException;

    /**
     * 根据用户id更改用户状态
     *
     * @param id     用户id
     * @param status 用户状态
     */
    void changeStatus(Long id, Boolean status);
}
