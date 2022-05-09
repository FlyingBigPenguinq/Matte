package com.syl.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.syl.annotation.Log;
import com.syl.dto.SysUserSaveOrUpdateDTO;
import com.syl.dto.SysUserSearchQueryDTO;
import com.syl.excel.sheet.SysUserImportSheet;
import com.syl.excel.sheet.SysUserSheet;
import com.syl.response.Response;
import com.syl.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Api(tags = "系统：用户")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
@Validated
public class SysUserController {
    private final ISysUserService iSysUserService;

    @Log("获取所有系统用户")
    @ApiOperation("获取所有系统用户")
    @PostMapping("/listSysUser/{page}/{size}")
    public Response listSysUser(@RequestBody SysUserSearchQueryDTO sysUserSearchQueryDTO,
                                @PathVariable Long page,
                                @PathVariable Long size) {
        return Response.success().setData(iSysUserService.listSysUser(sysUserSearchQueryDTO, page, size));
    }

    @Log("添加系统用户")
    @ApiOperation("添加系统用户")
    @PostMapping("/saveSysUser")
    public Response saveSysUser(@Validated({SysUserSaveOrUpdateDTO.Save.class})
                                    @RequestBody SysUserSaveOrUpdateDTO sysUserSaveOrUpdateDTO) {
        iSysUserService.saveSysUser(sysUserSaveOrUpdateDTO);
        return Response.success();
    }

    @Log("修改系统用户")
    @ApiOperation("修改系统用户")
    @PutMapping("/updateSysUser")
    public Response updateSysUser(@Validated({SysUserSaveOrUpdateDTO.Update.class})
                                      @RequestBody SysUserSaveOrUpdateDTO sysUserSaveOrUpdateDTO) {
        iSysUserService.updateSysUser(sysUserSaveOrUpdateDTO);
        return Response.success();
    }

    @Log("删除系统用户")
    @ApiOperation("删除系统用户")
    @DeleteMapping("/delSysUser")
    public Response delSysUser(@NotEmpty(message = "ID不能为空") @RequestBody Set<Long> id) {
        iSysUserService.delSysUser(id);
        return Response.success();
    }

    @Log("重置用户密码")
    @ApiOperation("重置用户密码")
    @PutMapping("/resetPassword/{id}")
    public Response resetPassword(@PathVariable Long id) {
        iSysUserService.resetPassword(id);
        return Response.success();
    }

    @Log("Excel导出用户")
    @ApiOperation("Excel导出用户")
    @PostMapping("/exportSysUser")
    public void exportSysUser(@Validated @RequestBody SysUserSearchQueryDTO sysUserSearchQueryDTO,
                              HttpServletResponse httpServletResponse) throws IOException {
        String fileName = "系统用户信息";
        setResponseHead(httpServletResponse, fileName, ".xlsx");
        // 获取数据
        List<SysUserSheet> sysUserSheetList = iSysUserService.listSysUserSheetsByQuery(sysUserSearchQueryDTO);
        // 导出
        EasyExcelFactory.write(httpServletResponse.getOutputStream())
                .head(SysUserSheet.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(fileName)
                .doWrite(sysUserSheetList);
    }

    @Log("Excel导出用户模板（用于导入）")
    @ApiOperation("Excel导出用户模板（用于导入）")
    @GetMapping("/exportSysUserTemplate")
    public void exportSysUserTemplate(HttpServletResponse httpServletResponse) throws IOException {
        String fileName = "系统用户信息模板";
        setResponseHead(httpServletResponse, fileName, ".xlsx");
        // 获取数据
        List<SysUserImportSheet> sysUserSheetList = iSysUserService.getSysUserSheetTemplate();
        // 导出
        EasyExcelFactory.write(httpServletResponse.getOutputStream())
                .head(SysUserImportSheet.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(fileName)
                .doWrite(sysUserSheetList);
    }

    @Log("Excel导入用户")
    @ApiOperation("Excel导入用户")
    @PostMapping("/importSysUser")
    public Response importSysUser(@RequestParam("file") MultipartFile excel) throws IOException {
        return Response.success().setData(iSysUserService.importSysUser(excel));
    }

    @Log("改变用户状态")
    @ApiOperation("改变用户状态")
    @PutMapping("/changeStatus/{id}/{status}")
    public Response changeStatus(@PathVariable Long id, @PathVariable Boolean status) {
        iSysUserService.changeStatus(id, status);
        return Response.success();
    }


    /**
     * 设置响应头
     *
     * @param httpServletResponse 响应
     * @param fileName            文件名
     * @param suffix              后缀
     * @throws UnsupportedEncodingException 异常
     */
    private void setResponseHead(HttpServletResponse httpServletResponse, String fileName, String suffix) throws UnsupportedEncodingException {
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8") + suffix);
    }

}
