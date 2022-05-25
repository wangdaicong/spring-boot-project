package cn.goitman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Nicky
 * @version 1.0
 * @className RedisLikeDesignApplication
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 引导类
 * @date 2022/5/12 15:55
 */
@SpringBootApplication
@EnableScheduling // 开启Scheduling注解
public class RedisLikeDesignApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisLikeDesignApplication.class, args);
    }
}
