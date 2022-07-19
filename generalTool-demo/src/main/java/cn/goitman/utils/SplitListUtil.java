package cn.goitman.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className SplitListUtils
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 分段拆解集合
 * @date 2022/7/8 10:06
 */
public class SplitListUtil {

    /**
     * @param [bigList 需拆解结合, subListSize 子集合长度]
     * @return java.util.List<java.util.List < T>> 子集合列表
     * @description 分段拆解集合
     */
    public static <T> List<List<T>> split(List<T> bigList, int subListSize) {
        // 判断集合是否为空或长度小于等于 0
        if (CollectionUtils.isEmpty(bigList) || subListSize <= 0) {
            return new ArrayList<>();
        }

        List<List<T>> resultList = new ArrayList<>();

        int size = bigList.size();
        // 长度不足subListLength处理
        if (size <= subListSize) {
            resultList.add(bigList);
        } else {
            // 大小等于subListSize的段数
            int parNum = size / subListSize;
            // 模数
            int modulus = size % subListSize;

            // subListSize大小集合封装处理
            for (int i = 0; i < parNum; i++) {
                List<T> parList = new ArrayList<>();
                for (int j = 0; j < subListSize; j++) {
                    parList.add(bigList.get(i * subListSize + j));
                }
                resultList.add(parList);
            }

            // 模数集合封装处理
            if (modulus > 0) {
                List<T> parList = new ArrayList<>();
                for (int i = 0; i < modulus; i++) {
                    parList.add(bigList.get(parNum * subListSize + i));
                }
                resultList.add(parList);
            }

        }
        return resultList;
    }
}