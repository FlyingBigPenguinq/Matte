package com.syl.aspect;

import com.syl.entity.SysLog;
import com.syl.service.ISysLogService;
import com.syl.util.ThrowableUtil;
import com.syl.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    private final ISysLogService sysLogService;

    public LogAspect(ISysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.syl.annotation.Log)")
    public void logPointCut() {
        // 定义切点
    }

    /**
     * 配置环绕通知
     *
     * @param joinPoint 切点
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("logPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        SysLog log = new SysLog("INFO", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        // 保存到日志中
        sysLogService.saveSysLog(WebUtil.getBrowser(request), WebUtil.getIp(request), joinPoint, log);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint 切点
     * @param e         Throwable
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog log = new SysLog("ERROR", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setExceptionDetail(ThrowableUtil.getStackTraceByPn(e, "com.gokzzz"));
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        // 保存到日志中
        sysLogService.saveSysLog(WebUtil.getBrowser(request), WebUtil.getIp(request), (ProceedingJoinPoint) joinPoint, log);
    }

}
