package com.scheduledtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Nicky
 * @version 1.0
 * @className scheduledTaskApplication
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 启动引导类
 * @date 2020/10/26 17:24
 */
@SpringBootApplication
@MapperScan("com.scheduledTask.mapper") // 指定扫描的Mapper类的包的路径，简化直接在每个Mapper类上添加注解@Mapper
public class ScheduledTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduledTaskApplication.class, args);
    }
}
