package com.bloomFiter.util;

/**
 * @author Nicky
 * @version 1.0
 * @className RedisBloomFilter
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Redis工具类
 * @date 2020/8/3 11:13
 */

import com.bloomFiter.config.BloomFilterHelper;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RedisBloomFilter<T> {
/*
    @Autowired
    private JedisCluster cluster;

    public RedisBloomFilter(JedisCluster jedisCluster) {
        this.cluster = jedisCluster;
    }*/

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加一个元素到布隆过滤器，可以设置返回值
     */
    public <T> void add(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        // Preconditions是guava提供的校验的工具类：校验表达式是否为真，不为真时显示指定的错误信息
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
            // cluster.setbit(key, i, true);
        }
    }

    /**
     * 批量添加元素到布隆过滤器，使用pipeline方式(批量性能高)
     * 缺点：没有返回值，不知道是否插入成功
     */
    public <T> void addList(BloomFilterHelper<T> bloomFilterHelper, String key, List<T> valueList) {
        redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (T value : valueList) {
                    int[] offset = bloomFilterHelper.murmurHashOffset(value);
                    for (int i : offset) {
                        redisConnection.setBit(key.getBytes(), i, true);
                    }
                }
                return null;
            }
        });
    }

    /**
     * 判断值在布隆过滤器中是否存在
     */
    public <T> boolean contains(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                // if (!cluster.getbit(key, i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 删除缓存的key
     */
    public void delete(String key) {
        if (Optional.ofNullable(key).isPresent()) {
            redisTemplate.delete(key);
        }
    }
}
