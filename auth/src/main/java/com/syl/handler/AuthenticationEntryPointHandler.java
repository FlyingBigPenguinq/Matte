package com.syl.handler;

import com.alibaba.fastjson.JSON;
import com.syl.response.Response;
import com.syl.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Liu XiangLiang
 * @description 身份入口认证失败处理器
 */
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        WebUtil.renderString(HttpStatus.UNAUTHORIZED.value(), httpServletResponse,
                JSON.toJSONString(Response.fail(HttpStatus.UNAUTHORIZED.value(), e.getLocalizedMessage())));
    }
}
