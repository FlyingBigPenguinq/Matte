package com.syl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Data
@ApiModel("角色所授权的用户查询条件")
public class SysRoleUserSearchQueryDTO {
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("名称")
    private String name;
}
