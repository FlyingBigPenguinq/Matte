package com.syl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Data
@ApiModel("系统角色DTO")
public class SysRoleSaveOrUpdateDTO {
    /* 分组校验 */
    public @interface Save {
    }

    /* 分组校验 */
    public @interface Update {
    }
    @NotNull(message = "系统角色id为空", groups = {Update.class})
    @ApiModelProperty("id")
    private Long id;

    @NotBlank(message = "名称为空", groups = {Save.class, Update.class})
    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @NotBlank(message = "角色标识为空", groups = {Save.class, Update.class})
    @ApiModelProperty("角色标识")
    private String code;
}
