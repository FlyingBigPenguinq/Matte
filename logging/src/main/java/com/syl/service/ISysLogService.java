package com.syl.service;

import com.syl.dto.SysLogSearchQueryDTO;
import com.syl.entity.SysLog;
import com.syl.response.PageData;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
public interface ISysLogService {

    /**
     * 保存日志
     *
     * @param browser   浏览器
     * @param ip        IP
     * @param joinPoint 切点
     * @param sysLog    日志对象
     */
    void saveSysLog(String browser, String ip, ProceedingJoinPoint joinPoint, SysLog sysLog);

    /**
     * 查询日志列表
     *
     * @param sysLogSearchQueryDTO 查询条件
     * @param page                 分页对象
     * @param size                 每页显示条数
     * @return 日志列表
     */
    PageData<SysLog> listSysLogByPage(SysLogSearchQueryDTO sysLogSearchQueryDTO, Long page, Long size);

    /**
     * 删除全部日志
     */
    void delAllSysLog();
}
