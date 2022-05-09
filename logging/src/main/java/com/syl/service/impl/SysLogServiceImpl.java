package com.syl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.annotation.Log;
import com.syl.dto.SysLogSearchQueryDTO;
import com.syl.entity.SysLog;
import com.syl.mapper.SysLogMapper;
import com.syl.response.PageData;
import com.syl.service.ISysLogService;
import com.syl.util.WebUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Service
@RequiredArgsConstructor
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    private final SysLogMapper sysLogMapper;

    @Override
    public void saveSysLog(String browser, String ip, ProceedingJoinPoint joinPoint, SysLog sysLog) {
        noNull(browser, ip, joinPoint, sysLog);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);
        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
        setByParams(browser, ip, joinPoint, sysLog, method, aopLog, methodName);
        sysLogMapper.insert(sysLog);
    }

    @Override
    public PageData<SysLog> listSysLogByPage(SysLogSearchQueryDTO sysLogSearchQueryDTO, Long page, Long size) {
        Assert.notNull(page, "page不能为空");
        Assert.notNull(size, "size不能为空");
        Assert.notNull(sysLogSearchQueryDTO, "日志查询条件不能为空");
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CharSequenceUtil.isNotBlank(sysLogSearchQueryDTO.getLogType()), SysLog::getLogType, sysLogSearchQueryDTO.getLogType());
        if (CollUtil.isNotEmpty(sysLogSearchQueryDTO.getCreateTime()) && ObjectUtil.isNotNull(sysLogSearchQueryDTO.getCreateTime())) {
            wrapper.between(SysLog::getCreateTime, sysLogSearchQueryDTO.getCreateTime().get(0), sysLogSearchQueryDTO.getCreateTime().get(1));
        }
        Page<SysLog> sysLogPage = sysLogMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageData<>(sysLogPage.getCurrent(), sysLogPage.getSize(), sysLogPage.getTotal(), sysLogPage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllSysLog() {
        sysLogMapper.deleteAll();
    }

    /**
     * 根据方法和传入的参数获取请求参数
     *
     * @param method 方法
     * @param args   参数
     * @return 请求参数
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.isEmpty()) {
            return "";
        }
        return argList.size() == 1 ? JSONUtil.toJsonStr(argList.get(0)) : JSONUtil.toJsonStr(argList);
    }

    /**
     * 参数校验
     *
     * @param browser   浏览器
     * @param ip        IP地址
     * @param joinPoint 切点
     * @param sysLog    日志
     */
    private void noNull(String browser, String ip, ProceedingJoinPoint joinPoint, SysLog sysLog) {
        Assert.notNull(browser, "浏览器不能为空");
        Assert.notNull(ip, "ip不能为空");
        Assert.notNull(joinPoint, "joinPoint不能为空");
        Assert.notNull(sysLog, "日志不能为空");
    }

    /**
     * 设置日志
     *
     * @param browser    浏览器
     * @param ip         IP地址
     * @param joinPoint  切点
     * @param sysLog     日志
     * @param method     方法
     * @param aopLog     注解
     * @param methodName 方法路径
     */
    private void setByParams(String browser, String ip, ProceedingJoinPoint joinPoint, SysLog sysLog, Method method, Log aopLog, String methodName) {
        sysLog.setDescription(aopLog.value());
        sysLog.setRequestIp(ip);
        sysLog.setAddress(WebUtil.getLocalCityInfo(sysLog.getRequestIp()));
        sysLog.setMethod(methodName);
        sysLog.setParams(getParameter(method, joinPoint.getArgs()));
        sysLog.setBrowser(browser);
    }

}
