package com.bloomFilter;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Nicky
 * @version 1.0
 * @className BloomFilterDemo
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Guava实现布隆过滤器
 * @date 2020/7/17 14:40
 */
public class BloomFilterDemo {
    // 插入布隆过滤器的初始化数据量长度
    private static final int insertions = 1000000;

    //期望的误判率
    private static double fpp = 0.01;

    public static void main(String[] args) {

        /*
         * 初始化一个存储string类型数据的布隆过滤器，误判率为0.1
         * @param 存储类型
         * @param 初始化数据量长度
         * @param 误判率
         */
        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), insertions, fpp);

        //用于存放所有实际存在的key，用于判断是否存在
        HashSet<String> sets = new HashSet<>(insertions);
        //用于存放所有实际存在的key，用于取出
        List<String> lists = new ArrayList<>(insertions);

        // 创建数据，插入随机字符串
        for (int i = 0; i < insertions; i++) {
            String uuid = UUID.randomUUID().toString();
            bf.put(uuid);
            sets.add(uuid);
            lists.add(uuid);
        }

        // 正确数量
        int rigthNum = 0;
        // 错误数量
        int wrongNum = 0;

        for (int i = 0; i < 10000; i++) {
            // 0-10000之间，可以被100整除的数有100个（100的倍数）
            String data = i % 100 == 0 ? lists.get(i % 100) : UUID.randomUUID().toString();

            // 先判断此数据在布隆过滤器中是否存在(有误判性)
            if (bf.mightContain(data)) {
                // 如果数据在布隆过滤器中存在，再判断在sets集合中是否存在
                if (sets.contains(data)) {
                    rigthNum++;
                    continue;
                }
                wrongNum++;
            }
        }

        // 被除数
        BigDecimal num = new BigDecimal(9900);

        // 计算误判率，保留两位小数，向“最接近的”数字四舍五入
        BigDecimal percet = new BigDecimal(wrongNum).divide(num, 2, RoundingMode.HALF_UP);
        // 计算命中率，保留两位小数，向“最接近的”数字四舍五入
        BigDecimal bingo = new BigDecimal(9900 - wrongNum).divide(num, 2, RoundingMode.HALF_UP);

        System.err.println("在100W个元素中，判断100个实际存在的元素，布隆过滤器认为存在的:" + rigthNum);
        System.err.println("在100W个元素中，判断9900个实际不存在的元素，误认为存在的:" + wrongNum + " 命中率:" + bingo + " 误判率:" + percet);
    }


}
