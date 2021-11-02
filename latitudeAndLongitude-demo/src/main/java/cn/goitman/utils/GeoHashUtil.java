package cn.goitman.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className GeoHashUtil
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description GeoHash编码类
 * @date 2021/10/28 10:15
 */
public class GeoHashUtil {

    // 最大经度
    public final double Max_Lng = 180;
    // 最小经度
    public final double Min_Lng = -180;
    // 最大纬度
    public final double Max_Lat = 90;
    // 最小纬度
    public final double Min_Lat = -90;

    // 经度或纬度二进制长度
    private final int length = 20;

    private final double lngUnit = (Max_Lng - Min_Lng) / (1 << 20);
    private final double latUnit = (Max_Lat - Min_Lat) / (1 << 20);

    // 用0-9、b-z（去掉a, i, l, o，分别代表十进制数10 ~ 31）这32个字母进行编码
    private final String[] base32Lookup =
            {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "b", "c", "d", "e", "f", "g", "h",
                    "j", "k", "m", "n", "p", "q", "r",
                    "s", "t", "u", "v", "w", "x", "y", "z"};

    /**
     * 将经度或纬度转换为二进制编码，递归二分区间法
     */
    private void convert(double min, double max, double value, List<Character> list) {
        if (list.size() > (length - 1)) {
            return;
        }
        double mid = (max + min) / 2;
        if (value < mid) {
            // 左区间
            list.add('0');
            convert(min, mid, value, list);
        } else {
            // 右区间
            list.add('1');
            convert(mid, max, value, list);
        }
    }

    /**
     * Base32编码长度为：经度和纬度二进制长度相加 lng + late = num，再除于5(5个二进制位转换成一个base32码)，如(20 + 20) / 5 = 8
     */
    private String base32Encode(final String str) {
        String unit = "";
        StringBuilder sb = new StringBuilder();
        for (int start = 0; start < str.length(); start = start + 5) {
            // 截取5个二进制位
            unit = str.substring(start, start + 5);
            // 根据十进制数，获取Base32编码表中对应值
            sb.append(base32Lookup[convertToIndex(unit)]);
        }
        return sb.toString();
    }

    /**
     * 将5个二进制位转化成十进制数
     */
    private int convertToIndex(String str) {
        int length = str.length();
        int result = 0;
        for (int index = 0; index < length; index++) {
            result += str.charAt(index) == '0' ? 0 : 1 << (length - 1 - index);
        }
        return result;
    }

    /**
     * 先合并经纬度的二进制编码（经度占偶数位，纬度占奇数位，0也是偶数位），后Base32编码
     */
    public String encode(double lng, double lat) {
        List<Character> lngList = new ArrayList<Character>();
        List<Character> latList = new ArrayList<Character>();
        convert(Min_Lng, Max_Lng, lng, lngList);
        convert(Min_Lat, Max_Lat, lat, latList);
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < latList.size(); index++) {
            sb.append(lngList.get(index)).append(latList.get(index));
        }
        return base32Encode(sb.toString());
    }

    /**
     * 边界问题，根据经纬度计算出原点及周围8个区域的Geohash值
     */
    public List<String> around(double lng, double lat) {
        List<String> list = new ArrayList<String>();
        list.add(encode(lng, lat + latUnit));
        list.add(encode(lng, lat - latUnit));
        list.add(encode(lng + lngUnit, lat));
        list.add(encode(lng - lngUnit, lat));
        // 原点
        list.add(encode(lng, lat));
        list.add(encode(lng + lngUnit, lat + latUnit));
        list.add(encode(lng - lngUnit, lat + latUnit));
        list.add(encode(lng + lngUnit, lat - latUnit));
        list.add(encode(lng - lngUnit, lat - latUnit));
        return list;
    }

    public static void main(String[] args) {
        System.out.println(new GeoHashUtil().encode(116.3967, 44.9999));
        System.out.println(new GeoHashUtil().around(116.3967, 44.9999));
    }
}