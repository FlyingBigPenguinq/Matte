package com.syl.controller;

import com.syl.annotation.Log;
import com.syl.dto.SysRoleSaveOrUpdateDTO;
import com.syl.dto.SysRoleSearchQueryDTO;
import com.syl.dto.SysRoleUserSearchQueryDTO;
import com.syl.response.Response;
import com.syl.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Api(tags = "系统：角色")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
@Validated
public class SysRoleController {
    private final ISysRoleService sysRoleService;

    @Log("获取所有系统角色带分页查询")
    @ApiOperation("获取所有系统角色带分页查询")
    @PostMapping("/listSysRole/{page}/{size}")
    public Response listSysUser(@RequestBody SysRoleSearchQueryDTO sysRoleSearchQueryDTO,
                                @PathVariable Long page,
                                @PathVariable Long size) {
        return Response.success().setData(sysRoleService.listSysRole(sysRoleSearchQueryDTO, page, size));
    }

    @Log("添加系统角色")
    @ApiOperation("添加系统角色")
    @PostMapping("/saveSysRole")
    public Response saveSysRole(@Validated({SysRoleSaveOrUpdateDTO.Save.class})
                                    @RequestBody SysRoleSaveOrUpdateDTO sysRoleSaveOrUpdateDTO) {
        sysRoleService.saveSysRole(sysRoleSaveOrUpdateDTO);
        return Response.success();
    }

    @Log("修改系统角色")
    @ApiOperation("修改系统角色")
    @PutMapping("/updateSysRole")
    public Response updateSysRole(@Validated({SysRoleSaveOrUpdateDTO.Update.class})
                                      @RequestBody SysRoleSaveOrUpdateDTO sysRoleSaveOrUpdateDTO) {
        sysRoleService.updateSysRole(sysRoleSaveOrUpdateDTO);
        return Response.success();
    }

    @Log("删除系统角色")
    @ApiOperation("删除系统角色")
    @DeleteMapping("/delSysRole")
    public Response delSysRole(@NotEmpty(message = "ID不能为空") @RequestBody Set<Long> id) {
        sysRoleService.delSysUser(id);
        return Response.success();
    }

    @Log("获取所有授权系统用户")
    @ApiOperation("获取所有授权系统用户")
    @PostMapping("/auth/listSysUser/{id}/page/{page}/{size}")
    public Response listAuthSysUser(@RequestBody SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO,
                                    @PathVariable Long id,
                                    @PathVariable Long page,
                                    @PathVariable Long size) {
        return Response.success().setData(sysRoleService.listAuthSysUserByRoleIdAndPageQuery(sysRoleUserSearchQueryDTO, id, page, size));
    }

    @Log("获取所有未授权系统用户")
    @ApiOperation("获取所有未授权系统用户")
    @PostMapping("/unAuth/listSysUser/{id}/page/{page}/{size}")
    public Response listUnAuthSysUser(@RequestBody SysRoleUserSearchQueryDTO sysRoleUserSearchQueryDTO,
                                      @PathVariable Long id,
                                      @PathVariable Long page,
                                      @PathVariable Long size) {
        return Response.success().setData(sysRoleService.listUnAuthSysUserByRoleIdAndPageQuery(sysRoleUserSearchQueryDTO, id, page, size));
    }

    @Log("授权系统用户")
    @ApiOperation("授权系统用户")
    @PostMapping("/authSysUser/{id}")
    public Response authSysUser(@NotEmpty(message = "ID不能为空") @RequestBody Set<Long> userIds,
                                @PathVariable("id") Long roleId) {
        sysRoleService.authSysUser(userIds, roleId);
        return Response.success();
    }

    @Log("取消授权系统用户")
    @ApiOperation("取消授权系统用户")
    @DeleteMapping("/unAuthSysUser/{id}")
    public Response unAuthSysUser(@NotEmpty(message = "ID不能为空") @RequestBody Set<Long> userIds,
                                  @PathVariable("id") Long roleId) {
        sysRoleService.unAuthSysUser(userIds, roleId);
        return Response.success();
    }

    @Log("获取所有系统角色")
    @ApiOperation("获取所有系统角色")
    @GetMapping("/getSysRole")
    public Response getSysRole() {
        return Response.success().setData(sysRoleService.getSysRole());
    }

    @Log("根据角色id获取对应的菜单")
    @ApiOperation("根据角色id获取对应的菜单")
    @GetMapping("/getSysMenu/{id}")
    public Response getSysMenu(@PathVariable("id") Long id) {
        return Response.success().setData(sysRoleService.getSysMenu(id));
    }

    @Log("根据角色id添加对应的菜单")
    @ApiOperation("根据角色id添加对应的菜单")
    @PostMapping("/saveSysMenu/{id}")
    public Response saveSysMenu(@PathVariable("id") Long id, @NotNull(message = "菜单id值为空") @RequestBody Set<Long> menuIds) {
        sysRoleService.saveSysMenu(id, menuIds);
        return Response.success();
    }
}
