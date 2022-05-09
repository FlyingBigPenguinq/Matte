package com.syl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Data
@ApiModel("菜单查询条件DTO")
public class SysMenuSearchQueryDTO {
    @ApiModelProperty("菜单名称")
    private String title;
    @ApiModelProperty("菜单是否隐藏")
    private Boolean hidden;
}
