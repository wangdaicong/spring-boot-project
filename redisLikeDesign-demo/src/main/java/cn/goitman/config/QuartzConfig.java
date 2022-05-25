package cn.goitman.config;

import cn.goitman.task.LikeTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nicky
 * @version 1.0
 * @className QuartzConfig
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Quartz 配置类
 * @date 2022/5/23 11:00
 */
//@Configuration
public class QuartzConfig {

    private static final String LIKE_TASK_IDENTITY = "LikeTask";

    @Bean
    public JobDetail jobDatail() {
        return JobBuilder.newJob(LikeTask.class)  // 对应Job
                .withIdentity(LIKE_TASK_IDENTITY)  // 给JobDetail起个id
                .storeDurably()  // 即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
    }

    @Bean
    public Trigger trigger() {
        // 定时任务配置（SimpleScheduleBuilder 简单构建器、CronScheduleBuilder Cron构建器）
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds()  // 以"秒"为单位执行
//                .withIntervalInHours()  // 以"时"为单位执行
//                .withRepeatCount(1)   // 执行次数（如果没配置任务开始时间，会在创建触发器时就触发一次（n+1））
                .withIntervalInMinutes(1)  // 以"分"为单位执行
                .repeatForever();   // 指定触发器无限期重复

//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(jobDatail())    // 关联JobDetail
                .withIdentity(LIKE_TASK_IDENTITY)   // 给Trigger起个id
                .withSchedule(simpleScheduleBuilder)    // 关联任务配置
//                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
