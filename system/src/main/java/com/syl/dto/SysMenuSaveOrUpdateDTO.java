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
@ApiModel("菜单保存或更新DTO")
public class SysMenuSaveOrUpdateDTO {
    /* 分组校验 */
    public @interface Save {
    }

    /* 分组校验 */
    public @interface Update {
    }

    @NotNull(message = "菜单ID不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    private Long id;

    @NotBlank(message = "名称不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty("菜单标题")
    private String title;

    @ApiModelProperty("上级菜单")
    private Long pid;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("权限标识")
    private String permission;

    @NotNull(message = "名称不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty("菜单类型，0:目录、1:菜单、2:按钮")
    private Integer type;

    @NotNull(message = "名称不能为空", groups = {Save.class, Update.class})
    @ApiModelProperty("是否隐藏，0:否、1:是")
    private Boolean hidden;
}
