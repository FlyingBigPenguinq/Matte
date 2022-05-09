package com.syl.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Data
@ApiModel("封装分页数据")
@NoArgsConstructor
public class PageData<T> {
    @ApiModelProperty("页码")
    Long currentPage = 1L;
    @ApiModelProperty("页数")
    Long size = 10L;
    @ApiModelProperty("总数")
    Long total = 0L;
    @ApiModelProperty("记录")
    List<T> records;

    public PageData(Long currentPage, Long size, Long total, List<T> records) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
        this.records = records;
    }

}
