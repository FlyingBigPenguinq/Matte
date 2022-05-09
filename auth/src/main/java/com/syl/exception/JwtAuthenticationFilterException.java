package com.syl.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Liu XiangLiang
 * @description jwt过滤器异常
 */
public class JwtAuthenticationFilterException extends AuthenticationException {

    private static final long serialVersionUID = -3914361857661585612L;

    public JwtAuthenticationFilterException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationFilterException(String msg) {
        super(msg);
    }
}
