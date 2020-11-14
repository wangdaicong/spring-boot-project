package cn.goitman.task;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Nicky
 * @version 1.0
 * @className ScheduledTask
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 同步处理任务类
 * @date 2020/10/27 15:52
 */
public class ScheduledTask {
    // 使用volatile同步机制，处理定时任务
    public volatile ScheduledFuture future;

    /**
     * @return void
     * @method cancel
     * @description 取消定时任务
     */
    public void cancel() {
        ScheduledFuture future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
