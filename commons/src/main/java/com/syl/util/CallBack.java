package com.syl.util;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 * @description SpringContext回调
 */
public interface CallBack {
    /**
     * 回调执行方法
     */
    void executor();

    /**
     * 本回调任务名称
     * @return /
     */
    default String getCallBackName() {
        return Thread.currentThread().getId() + ":" + this.getClass().getName();
    }
}
