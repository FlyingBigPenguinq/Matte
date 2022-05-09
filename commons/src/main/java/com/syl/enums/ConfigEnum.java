package com.syl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigEnum {

    // 默认密码
    DEFAULT_PASSWORD("123456");

    private final String value;
}
