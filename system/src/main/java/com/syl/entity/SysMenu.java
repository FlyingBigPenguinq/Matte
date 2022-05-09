package com.syl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.syl.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("菜单")
public class SysMenu extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

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

    @ApiModelProperty("菜单类型，0:目录、1:菜单、2:按钮")
    private Integer type;

    @ApiModelProperty("是否隐藏，0:否、1:是")
    private Boolean hidden;

    @TableField(exist = false)
    private List<SysMenu> children;
}
