package com.syl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.syl.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("日志")
@Getter
@Setter
@NoArgsConstructor
public class SysLog extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("方法名")
    private String method;

    @ApiModelProperty("参数")
    private String params;

    @ApiModelProperty("日志类型")
    private String logType;

    @ApiModelProperty("请求ip")
    private String requestIp;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("请求耗时")
    private Long time;

    @ApiModelProperty("异常详细")
    private String exceptionDetail;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
