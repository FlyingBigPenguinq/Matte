package com.syl.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Liu XiangLiang
 */
public class LoginUserAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = 4274357085945308790L;

    public LoginUserAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginUserAuthenticationException(String msg) {
        super(msg);
    }
}
