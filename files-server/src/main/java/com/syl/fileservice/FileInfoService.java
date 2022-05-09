package com.syl.fileservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Liu XiangLiang
 * @description: Files Service Interfaces
 * @date 2022/5/2 下午3:48
 */
public interface FileInfoService extends IService<FileInfo> {

    List<String> upload(MultipartFile[] multipartFiles, String applicationName) throws Exception;

    String download(List<String> fileList) throws Exception;

    Page<FileInfo> findFiles(Page<FileInfo> fileInfoPage, Map<String, Object> params);
}
