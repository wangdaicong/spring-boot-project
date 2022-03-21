package cn.goitman.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Nicky
 * @version 1.0
 * @className Restrict
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 限流控制注解
 * @date 2022/3/16 15:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Restrict {

    // 业务标识；不同接口，不同流量控制
    String key() default "";

    // 访问限制数
    long limitedNumber() default 1;

    // 过期时间，单位：秒
    long expire() default 10;

    // 取令牌最大等待时间
    long timeout() default 500;

    // 最大等待时间单位(毫秒)
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    // 提示信息
    String msg() default "活动火爆，请稍候再试！";
}
