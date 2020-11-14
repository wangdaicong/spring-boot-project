package cn.goitman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Nicky
 * @version 1.0
 * @className ScheduledConfig
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 配置线程池
 * @date 2020/10/27 14:48
 */
@Configuration
public class SchedulingConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        // 创建任务调度线程池
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 初始化线程池数量
        taskScheduler.setPoolSize(4);
        // 是否将取消后的任务，从队列中删除
        taskScheduler.setRemoveOnCancelPolicy(true);
        // 设置线程名前缀
        taskScheduler.setThreadNamePrefix("ThreadPool-");
        return taskScheduler;
    }
}
