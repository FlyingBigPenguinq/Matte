package com.syl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 * @description 登录DTO
 */
@Data
@ApiModel("登录DTO")
public class LoginUserDTO {
    @NotBlank(message = "账号为空")
    @ApiModelProperty("账号")
    private String username;

    @NotBlank(message = "密码为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "验证码key为空")
    @ApiModelProperty("验证码key")
    private String key;

    @NotBlank(message = "验证码为空")
    @ApiModelProperty("验证码")
    private String code;

}
