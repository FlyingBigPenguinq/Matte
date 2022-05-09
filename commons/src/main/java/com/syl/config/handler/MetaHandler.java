package com.syl.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.syl.util.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author Liu XiangLiang
 * @description: 处理新增和更新的基础数据填充，配合BaseEntity和MyBatisPlusConfig使用
 * @date 2022/4/30 下午7:47
 */
public class MetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        String username = null;
        try {
            username = SecurityUtil.getCurrentUsername();
        }catch (Exception e){
            username = "system";
        }
        this.setFieldValByName("createBy", username, metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", username, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
