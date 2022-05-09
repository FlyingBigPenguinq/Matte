package com.syl.excel.sheet;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.syl.excel.converter.GenderConverter;
import com.syl.excel.converter.StatusConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("系统用户ExcelSheet")
public class SysUserSheet {

    @ExcelProperty("ID")
    @ColumnWidth(10)
    @ApiModelProperty("id")
    private Long id;

    @ExcelProperty("用户账号")
    @ColumnWidth(20)
    @ApiModelProperty("用户账号")
    private String username;

    @ExcelProperty("名称")
    @ColumnWidth(20)
    @ApiModelProperty("名称")
    private String name;

    @ExcelProperty(value = "性别", converter = GenderConverter.class)
    @ColumnWidth(10)
    @ApiModelProperty("性别，0:男，1:女")
    private Integer gender;

    @ExcelProperty("电话")
    @ColumnWidth(20)
    @ApiModelProperty("电话")
    private String tel;

    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    @ApiModelProperty("邮箱")
    private String email;

    @ExcelProperty("地址")
    @ColumnWidth(30)
    @ApiModelProperty("地址")
    private String address;

    @ExcelProperty("生日")
    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    @ApiModelProperty("生日")
    private Date birthday;

    @ExcelProperty(value = "状态", converter = StatusConverter.class)
    @ColumnWidth(10)
    @ApiModelProperty("状态，false:正常，true:禁止")
    private Boolean status;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;
}
