package com.syl.exception.handler;


import com.alibaba.fastjson.JSON;
import com.syl.exception.RunException;
import com.syl.response.Response;
import com.syl.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     *
     * @param runException 自定义异常
     * @return 错误信息JSON
     */
    @ExceptionHandler(RunException.class)
    public void runException(RunException runException, HttpServletResponse response) {
        log.error(runException.getMessage(), runException);
        WebUtil.renderString(runException.getStatus(), response,
                JSON.toJSONString(Response.fail(runException.getStatus(), runException.getMessage())));
    }

    /**
     * 处理所有接口数据验证异常
     *
     * @param e /
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        log.error(message, e);
        WebUtil.renderString(HttpStatus.BAD_REQUEST.value(), response,
                JSON.toJSONString(Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), message)));
    }

    /**
     * 处理单个参数校验异常
     *
     * @param e /
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException e, HttpServletResponse response) {
        String message = e.getLocalizedMessage().substring(e.getLocalizedMessage().indexOf(':') + 1).replace(" ", "");
        log.error(message, e);
        WebUtil.renderString(HttpStatus.BAD_REQUEST.value(), response,
                JSON.toJSONString(Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), message)));
    }

    /**
     * 处理断言异常
     *
     * @param e        /
     * @param response /
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentException(IllegalArgumentException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        WebUtil.renderString(HttpStatus.INTERNAL_SERVER_ERROR.value(), response,
                JSON.toJSONString(Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())));
    }
}
