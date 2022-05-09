package com.syl.fileservice.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.entity.FileInfo;
import com.syl.fileservice.FileInfoService;
import com.syl.mapper.FileInfoMapper;
import com.syl.util.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @author Liu XiangLiang
 * @description: impl of File Service
 * @date 2022/5/2 下午3:52
 */
@Service
@AllArgsConstructor
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    @Autowired
    private final FileInfoMapper fileInfoMapper;

    @Override
    public List<String> upload(MultipartFile[] multipartFiles, String applicationName) throws Exception {
        List<String> filePaths = new ArrayList<>();
        Assert.notEmpty(multipartFiles, "Files is null");
        for (MultipartFile multipartFile:multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();
            Assert.notNull(originalFilename, "file name is null");
            String fileName = System.currentTimeMillis() + originalFilename.substring(originalFilename.indexOf("."));
            String fileUrl = FileUtil.saveFile(multipartFile.getInputStream(), "/" + applicationName + "/" + fileName);

            String thumbFileName = "/thumbFile/" + fileName;
            String contentType = FileUtil.getFileType(originalFilename);
            String thumbFileUrl = "";
            if (Constant.FileType.FILE_IMG.equals(contentType)) {
                thumbFileUrl = fileUrl;
                //大于1m时启动图片压缩
                if(multipartFile.getSize() >= FileUtil.FILE_SIZE){
                    FileUtil.saveThumbFile(fileUrl, thumbFileName);
                    thumbFileUrl = thumbFileName;
                }

                FileInfo fileInfo = FileInfo.builder()
                        .id(UUID.randomUUID().toString().replaceAll("-",""))
                        .name(multipartFile.getOriginalFilename())
                        .applicationName(applicationName)
                        .contentType(contentType)
                        .size(multipartFile.getSize())
                        .thumbUrl(thumbFileUrl)
                        .url(fileUrl)
                        .createUser(SecurityUtil.getCurrentUser().getUsername())
                        .createTime(System.currentTimeMillis()).build();
                this.baseMapper.insert(fileInfo);
            }
        }
        return filePaths;
    }

    @Override
    public String download(List<String> urlList) throws Exception {
        if (ComUtil.isEmpty(urlList)) {
            return null;
        }
        String fileDir = FileUtil.fileUrl + "/zip/";
        FileUtil.createDir(fileDir);
        if (urlList.size() > 1) {//批量下载
            fileDir += DateTimeUtil.formatDateTimetoString(new Date(), "yyyyMMddHHmmss") + "/";
            FileUtil.createDir(fileDir);
            for (String url : urlList) {
                FileUtil.copy(new File(FileUtil.fileUrl+url), new File(fileDir +url.substring(url.lastIndexOf('/'))));
            }

            String fileZip = "文件信息" +DateTimeUtil.formatDateTimetoString(new Date(), "yyyyMMddHHmmss") + ".zip";
            FileUtil.fileToZip(fileDir, FileUtil.fileUrl + "/zip/", fileZip);
            FileUtil.deleteDirectory(fileDir);
            return "/zip/" + fileZip;
        } else {
            return urlList.get(0);
        }
    }

    @Override
    public Page<FileInfo> findFiles(Page<FileInfo> fileInfoPage, Map<String, Object> params) {
        List<FileInfo> fileList = fileInfoMapper.findFiles(fileInfoPage, params);
        return fileInfoPage.setRecords(fileList);
    }
}
