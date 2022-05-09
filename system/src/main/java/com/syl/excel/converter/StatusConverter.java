package com.syl.excel.converter;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
public class StatusConverter implements Converter<Boolean> {
    @Override
    public Class<?> supportJavaTypeKey() {
        //对象属性类型
        return Boolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        //CellData属性类型
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Boolean convertToJavaData(ReadConverterContext<?> context) throws Exception {
        //CellData转对象属性
        String cellStr = context.getReadCellData().getStringValue();
        if (CharSequenceUtil.isEmpty(cellStr)) return null;
        if ("男".equals(cellStr)) {
            return false;
        } else if ("女".equals(cellStr)) {
            return true;
        } else {
            return null;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Boolean> context) throws Exception {
        //对象属性转CellData
        Boolean cellValue = context.getValue();
        if (cellValue == null) {
            return new WriteCellData<>("");
        }
        if (!cellValue) {
            return new WriteCellData<>("正常");
        } else if (cellValue) {
            return new WriteCellData<>("禁止");
        } else {
            return new WriteCellData<>("");
        }
    }

}
