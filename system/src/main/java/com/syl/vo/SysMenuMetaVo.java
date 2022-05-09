package com.syl.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 23:12
 * @description SysMenuMetaVo
 */
@Data
@ApiModel("菜单meta参数")
public class SysMenuMetaVo {
    @ApiModelProperty("菜单标题")
    private String title;

    @ApiModelProperty("icon标题")
    private String icon;

}
