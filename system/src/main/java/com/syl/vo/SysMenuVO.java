package com.syl.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 * @description 前端菜单路由
 */
@Data
@ApiModel("菜单VO")
public class SysMenuVO {
    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("访问地址")
    private String path;

    @ApiModelProperty("是否隐藏，0：不隐藏，1：隐藏")
    private Boolean hidden;

    @ApiModelProperty("组件地址")
    private String component;

    @ApiModelProperty("菜单meta参数")
    private SysMenuMetaVo meta;

    @ApiModelProperty("子节点")
    private List<SysMenuVO> children;
}
