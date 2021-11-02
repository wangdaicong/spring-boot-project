package cn.goitman.utils;

/**
 * @author Nicky
 * @version 1.0
 * @className GeoHashUtil
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 两点距离类
 * @date 2021/10/28 10:15
 */
public class DistanceUtil {
    // 地球半径，单位：KM
    private final static double Earth_Radius = 6378.137d;

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        // 两点经纬度转换为三维直角坐标
        double x1 = Math.cos(lat1) * Math.cos(lng1);
        double y1 = Math.cos(lat1) * Math.sin(lng1);
        double z1 = Math.sin(lat1);

        double x2 = Math.cos(lat2) * Math.cos(lng2);
        double y2 = Math.cos(lat2) * Math.sin(lng2);
        double z2 = Math.sin(lat2);

        // 求两点间的弦长距离
        double chordLength = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2));
        // 由弦长求两点间的弧长距离
        double arcLength = Earth_Radius * Math.PI * 2 * Math.asin(0.5 * chordLength) / 180;

        return arcLength;
    }

    public static void main(String[] args) {
        String str = null;
        double distance = DistanceUtil.distance(44.9999, 116.3967, 45.0001, 116.3967);

        str = String.format("两点距离为 %f KM", distance);
        System.out.println(str);

        str = String.format("两点距离为 %s M", Math.round(distance * 1000));
        System.out.println(str);
    }
}