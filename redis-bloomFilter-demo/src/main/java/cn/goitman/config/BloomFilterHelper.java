package cn.goitman.config;

/**
 * @author Nicky
 * @version 1.0
 * @className BloomFilterHelper
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 布隆过滤器核心类
 * @date 2020/8/3 11:11
 */

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class BloomFilterHelper<T> {
    // hash循环次数
    private int numHashFunctions;
    // bit数组长度
    private int bitSize;
    // 布隆过滤器对象
    private Funnel<T> funnel;

    /**
     * @param [funnel 布隆过滤器对象, expectedInsertions 期望插入长度, fpp 误差率]
     */
    public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel不能为空");
        this.funnel = funnel;
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    /**
     * 布隆过滤器的实现基础：哈希算法，用于哈希检索操作
     * @param [value]
     */
    public int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }

        return offset;
    }

    /**
     * 计算bit数组长度
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算hash方法执行次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

}
