package com.syl.controller;

import com.syl.dto.SysLogSearchQueryDTO;
import com.syl.response.Response;
import com.syl.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */

@Api(tags = "系统：日志")
@RestController
@RequestMapping("/system/log")
@RequiredArgsConstructor
public class SysLogController {

    private final ISysLogService sysLogService;

    @ApiOperation("获取所有日志带分页查询")
    @PostMapping("/listSysLogByPage/{page}/{size}")
    public Response listSysLogByPage(@RequestBody SysLogSearchQueryDTO sysLogSearchQueryDTO,
                                     @PathVariable Long page,
                                     @PathVariable Long size) {
        return Response.success().setData(sysLogService.listSysLogByPage(sysLogSearchQueryDTO, page, size));
    }

    @ApiOperation("删除所有日志")
    @DeleteMapping ("/delAllSysLog")
    public Response listSysUser() {
        sysLogService.delAllSysLog();
        return Response.success();
    }




}

