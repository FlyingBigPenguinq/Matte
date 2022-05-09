package com.syl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisCollectionEnum {

    /**
     * 验证码
     */
    CAPTCHA_KEY("captcha");

    private final String key;
}
