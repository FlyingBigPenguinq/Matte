package com.syl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Data
@ApiModel("角色查询条件")
public class SysRoleSearchQueryDTO {
    @ApiModelProperty("角色名称")
    private String name;
    @ApiModelProperty("角色标识")
    private String code;
}
