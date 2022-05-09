package com.syl.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.syl.entity.LoginUser;
import com.syl.exception.JwtAuthenticationFilterException;
import com.syl.response.Response;
import com.syl.util.JwtUtil;
import com.syl.util.RedisUtil;
import com.syl.util.WebUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Liu XiangLiang
 * @description JWT 过滤器
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(jwtUtil.getHeader());
        //不带jwt token就不走此过滤器验证token，让它去匿名访问过滤器，
        //看是否是白名单中的url，如果不是白名单中的url，就会抛异常，去登录页
        if (CharSequenceUtil.isBlankOrUndefined(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Claims claim = jwtUtil.getClaimByToken(jwt);
            if (claim == null) {
                throw new JwtAuthenticationFilterException("token异常");
            }
            if (jwtUtil.isTokenExpired(claim)) {
                throw new JwtAuthenticationFilterException("token已过期");
            }
            String userId = claim.getSubject();
            // 获取用户的权限等信息
            LoginUser loginUser = JSON.parseObject(JSON.toJSONString(redisUtil.get("login:" + userId)), LoginUser.class);

            if (ObjectUtil.isNull(loginUser)) {
                throw new JwtAuthenticationFilterException("登录信息已过期，请重新登录");
            }
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationFilterException e) {
            logger.error(e.getLocalizedMessage());
            SecurityContextHolder.clearContext();
            WebUtil.renderString(HttpStatus.UNAUTHORIZED.value(),response,
                    JSON.toJSONString(Response.fail(HttpStatus.UNAUTHORIZED.value(), e.getLocalizedMessage())));
        }
    }
}
