package com.syl.controller;

import com.syl.annotation.Log;
import com.syl.dto.SysMenuSaveOrUpdateDTO;
import com.syl.dto.SysMenuSearchQueryDTO;
import com.syl.response.Response;
import com.syl.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Api(tags = "系统：菜单")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {
    private final ISysMenuService sysMenuService;

    @Log("获取所有菜单")
    @ApiOperation("获取所有菜单")
    @PostMapping("/listSysMenu")
    public Response listSysMenu(@RequestBody SysMenuSearchQueryDTO sysMenuSearchQueryDTO) {
        return Response.success().setData(sysMenuService.listSysMenu(sysMenuSearchQueryDTO));
    }

    @Log("保存菜单")
    @ApiOperation("保存菜单")
    @PostMapping("/saveSysMenu")
    public Response saveSysMenu(@Validated({SysMenuSaveOrUpdateDTO.Save.class}) @RequestBody SysMenuSaveOrUpdateDTO sysMenuSaveOrUpdateDTO) {
        sysMenuService.saveSysMenu(sysMenuSaveOrUpdateDTO);
        return Response.success();
    }

    @Log("更新菜单")
    @ApiOperation("更新菜单")
    @PutMapping("/updateSysMenu")
    public Response updateSysMenu(@Validated({SysMenuSaveOrUpdateDTO.Update.class}) @RequestBody SysMenuSaveOrUpdateDTO sysMenuSaveOrUpdateDTO) {
        sysMenuService.updateSysMenu(sysMenuSaveOrUpdateDTO);
        return Response.success();
    }

    @Log("删除菜单")
    @ApiOperation("删除菜单")
    @DeleteMapping("/deleteSysMenu/{id}")
    public Response deleteSysMenu(@PathVariable Long id) {
        sysMenuService.deleteSysMenu(id);
        return Response.success();
    }

    @Log("获取当前菜单下拉框数据")
    @ApiOperation("获取当前菜单下拉框数据")
    @GetMapping("/listSysMenuToSelect/{id}")
    public Response listSysMenuToSelect(@PathVariable Long id) {
        return Response.success().setData(sysMenuService.listSysMenuToSelect(id));
    }

    @Log("获取所有菜单下拉框数据")
    @ApiOperation("获取所有菜单下拉框数据")
    @GetMapping("/listSysMenuAllToSelect")
    public Response listSysMenuAllToSelect() {
        return Response.success().setData(sysMenuService.listSysMenuAllToSelect());
    }


}
