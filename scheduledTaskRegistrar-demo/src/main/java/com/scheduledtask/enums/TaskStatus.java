package com.scheduledtask.enums;

import lombok.AllArgsConstructor;

/**
 * @author Nicky
 * @version 1.0
 * @className TaskStatus
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 任务状态枚举
 * @date 2020/10/28 11:15
 */
@AllArgsConstructor // 全参构造
public enum TaskStatus {
    SUSPEND("暂停", 0),
    NORMAL("正常", 1);

    private String desc;
    private int value;

    public String desc() {
        return this.desc;
    }

    public int value() {
        return this.value;
    }
}
