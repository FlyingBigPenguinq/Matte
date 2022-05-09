package com.syl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 */
@Data
@ApiModel("用户VO")
public class SysUserVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户账号")
    private String username;

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

    @ApiModelProperty("状态，0:正常，1:禁止")
    private Boolean status;

    @ApiModelProperty("角色")
    private List<SysRoleVO> roles;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
}
