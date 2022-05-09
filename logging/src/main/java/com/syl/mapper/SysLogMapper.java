package com.syl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syl.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Repository
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
    /**
     * 删除所有日志
     */
    void deleteAll();
}
