package com.syl.handler;

import com.alibaba.fastjson.JSON;
import com.syl.response.Response;
import com.syl.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Liu XiangLiang
 * @description 权限不足处理器
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        WebUtil.renderString(HttpStatus.FORBIDDEN.value(),httpServletResponse,
                JSON.toJSONString(Response.fail(HttpStatus.FORBIDDEN.value(), "权限不足")));
    }
}
