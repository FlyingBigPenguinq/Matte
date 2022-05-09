package com.syl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Data
@ApiModel("用户编辑或保存的DTO")
public class SysUserSaveOrUpdateDTO {
    /* 分组校验 */
    public @interface Save {
    }
    /* 分组校验 */
    public @interface Update {
    }
    @NotNull(message = "用户ID不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    private Long id;

    @NotBlank(message = "用户账号为空", groups = {Save.class, Update.class})
    @ApiModelProperty("用户账号")
    private String username;

    @NotBlank(message = "名称为空", groups = {Save.class, Update.class})
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @NotNull(message = "性别为空", groups = {Save.class, Update.class})
    @ApiModelProperty("性别，0:男，1:女")
    private Integer gender;

    @NotNull(message = "状态为空", groups = {Save.class, Update.class})
    @ApiModelProperty("状态，0:正常，1:禁止")
    private Boolean status;

    @NotEmpty(message = "角色Id为空", groups = {Save.class, Update.class})
    @ApiModelProperty("角色Id集合")
    private Set<Long> roles;
}
