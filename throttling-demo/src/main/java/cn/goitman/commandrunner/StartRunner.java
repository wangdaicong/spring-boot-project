package cn.goitman.commandrunner;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Nicky
 * @version 1.0
 * @className StartRunner
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 预加载数据
 * @date 2022/3/16 16:48
 */
//@Component
//@Order(0)
public class StartRunner implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(StartRunner.class);

    public static final Map<String, RateLimiter> rateLimiterMap = Maps.newConcurrentMap();

    @Override
    public void run(String... args) throws Exception {
        RateLimiter rateLimiter = null;

        // 可改造为读取数据库
        Map<String, Integer> limitMap = new HashMap<>();
        limitMap.put("restrict1",1);
        limitMap.put("restrict2",2);

        Set<Map.Entry<String, Integer>> entries = limitMap.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            rateLimiter = RateLimiter.create(entry.getValue());
            log.info("创建令牌桶 : {}，大小为{}", entry.getKey(), entry.getValue());
            rateLimiterMap.put(entry.getKey(),rateLimiter);
        }
    }
}
