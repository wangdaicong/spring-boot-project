package com.bloomFiter;

import com.bloomFiter.config.BloomFilterHelper;
import com.bloomFiter.util.RedisBloomFilter;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className RedisBloomFilterApplication
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description
 * @date 2020/8/3 11:17
 */
@SpringBootApplication
@Slf4j
public class RedisBloomFilterApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(RedisBloomFilterApplication.class, args);
    }

    private static BloomFilterHelper<CharSequence> bloomFilterHelper;

    @Autowired
    public RedisBloomFilter redisBloomFilter;

    // 期望长度
    public int expectedInsertions = 100000;

    // 添加值
    public int value = 10000;

    // 误判率
    public double fpp = 0.01;

    /**
     * 初始化布隆过滤器
     */
    @PostConstruct
    public void init() {
        bloomFilterHelper = new BloomFilterHelper<>(Funnels.stringFunnel(Charsets.UTF_8), expectedInsertions, fpp);
        // bloomFilterHelper = new BloomFilterHelper<>(Funnels.stringFunnel(Charset.defaultCharset()), 1000, 0.01);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisBloomFilter.delete("bloom");
        int j = 0;

        // 添加10000个元素
        List<String> valueList = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            valueList.add(i + "");
        }

        long beginTime = System.currentTimeMillis();
        redisBloomFilter.addList(bloomFilterHelper, "bloom", valueList);
        long costMs = System.currentTimeMillis() - beginTime;
        log.info("布隆过滤器添加{}个值，耗时：{} s", value, costMs / 1000);

        for (int i = 0; i < expectedInsertions; i++) {
            boolean result = redisBloomFilter.contains(bloomFilterHelper, "bloom", i + "");
            if (!result) {
                j++;
            }
        }
        log.info("漏掉了{}个,验证结果耗时：{} s", j, (System.currentTimeMillis() - beginTime) / 1000);

        BigDecimal num = new BigDecimal(valueList.size());
        // 计算命中率，保留两位小数，向“最接近的”数字四舍五入
        BigDecimal bingo = new BigDecimal(expectedInsertions - j).divide(num, 2, RoundingMode.HALF_UP);
        log.info("命中率：{} %", bingo.doubleValue() * 100);
    }
}
