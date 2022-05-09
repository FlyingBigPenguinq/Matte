package com.syl.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Getter
public class RunException extends RuntimeException {
    private Integer status = INTERNAL_SERVER_ERROR.value();

    public RunException(String msg) {
        super(msg);
    }

    public RunException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }
}
