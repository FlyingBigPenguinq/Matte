package com.syl.entity;

import com.syl.vo.SysUserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Liu XiangLiang
 */
@Data
@ApiModel("用户相关信息")
public class UserInfo {
    @ApiModelProperty("用户信息")
    private SysUserVO user;
    @ApiModelProperty("用户权限")
    private List<String> permissions;
    @ApiModelProperty("用户角色")
    private List<String> roles;
}
