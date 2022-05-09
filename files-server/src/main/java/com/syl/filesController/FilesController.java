package com.syl.filesController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.entity.FileInfo;
import com.syl.fileservice.FileInfoService;
import com.syl.response.Response;
import com.syl.util.ComUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Liu XiangLiang
 * @description: Files  Controller
 * @date 2022/5/2 下午3:32
 */
@RestController
@AllArgsConstructor
public class FilesController {

    @Autowired
    private FileInfoService fileInfoService;

    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "文件数组", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "applicationName", value = "服务名称", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/upload")
    public Response<List<String>> upload(MultipartFile[] files, String applicationName) {
        Response<List<String>> response = new Response<>();
        try{
            List<String> filePaths = fileInfoService.upload(files, applicationName);
            response.setData(filePaths);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @ApiOperation(value = "下载/批量下载", notes = "需要header里加入Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contentType", value = "文件类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "applicationName", value = "服务名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "urls", value = "文件url逗号隔开", dataType = "String", paramType = "query")
    })
    @GetMapping("/download")
    public Response<Object> download(@RequestParam Map<String, Object> params) throws Exception {
        List<String> urlList = null;
        if (!ComUtil.isEmpty(params.get("urls"))) {
            String urls = params.get("urls").toString();
            if (urls.contains(",")) {
                urlList = Arrays.asList(urls.split(","));
            } else {
                return Response.success("success").setData(urlList);
            }

        } else {
            urlList = new ArrayList<>();
            List<String> finalUrlList = Lists.newArrayList();
            List<FileInfo> fileList = fileInfoService.findFiles(new Page<FileInfo>(1, Integer.MAX_VALUE), params).getRecords();
            fileList.forEach(file -> finalUrlList.add(file.getUrl()));
        }

        return Response.success("success").setData(urlList);
    }
}
