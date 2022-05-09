package com.syl.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.syl.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 * @description 用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("用户")
public class SysUser extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("电话")
    private String tel;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("生日")
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date birthday;

    @ApiModelProperty("性别，0:男，1:女")
    private Integer gender;

    @ApiModelProperty("状态，false:正常，true:禁止")
    private Boolean status;

}
