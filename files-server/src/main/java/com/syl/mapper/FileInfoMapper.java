package com.syl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件信息表 Mapper 接口
 * </p>
 *
 * @author liugh
 * @since 2019-06-13
 */
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {

    List<FileInfo> findFiles(Page<FileInfo> fileInfoPage, Map<String, Object> params);

    List<FileInfo> selectFilesByIds(@Param("ids") String[] ids);
}
