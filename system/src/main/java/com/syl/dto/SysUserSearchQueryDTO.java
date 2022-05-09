package com.syl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 * @description user查询条件DTO
 */
@Data
@ApiModel("查询用户搜索条件DTO")
public class SysUserSearchQueryDTO {

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("电话")
    private String tel;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("生日区间")
    private List<Date> birthday;

    @ApiModelProperty("性别，0:男，1:女")
    private Integer gender;

    @ApiModelProperty("状态，false:正常，true:禁止")
    private Boolean status;

}
