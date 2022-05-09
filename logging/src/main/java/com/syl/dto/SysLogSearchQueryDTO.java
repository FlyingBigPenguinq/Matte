package com.syl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */

@Data
@ApiModel("系统日志查询条件")
public class SysLogSearchQueryDTO {
    @ApiModelProperty("时间范围")
    private List<Date> createTime;
    @ApiModelProperty("日志类型")
    private String logType;
}
