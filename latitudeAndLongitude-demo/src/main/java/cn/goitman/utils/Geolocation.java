package cn.goitman.utils;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Nicky
 * @version 1.0
 * @className Geolocation
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 地理位置类
 * @date 2021/10/28 10:15
 */
public class Geolocation {
    /**
     * 百度地图密钥
     */
    static final String AK = "";
    /**
     * 百度地图API
     */
    static final String URL = "https://api.map.baidu.com/geocoding/v3/?output=json&";

    // static final String URL = "http://api.map.baidu.com/location/ip?&coor=bd09ll&";

    static Map<String, Object> map = new HashMap<String, Object>();
    static Map<String, String> hashMap = new HashMap<>();

    public static void main(String[] args) {
        String address = "广州天河城";
        Map<String, String> msgMap = getCoordinate(address);
        System.out.println("msgMap 数据：" + msgMap);
        System.out.println("'" + address + "'的经纬度为：" + msgMap.get("lng") + "，" + msgMap.get("lat"));

/*        String addr = "203.168.30.174";
        Map<String, String> msgMap = getCoordinate(addr);
        System.out.println("msgMap 数据：" + msgMap);
        System.out.println(String.format("IP地址区域为：%s；经纬度为：%s",
                StringUtils.isEmpty(msgMap.get("city")) ? msgMap.get("address") : msgMap.get("city"),
                msgMap.get("x") + "，" + msgMap.get("y")));*/
    }

    /**
     * 根据地址，获取相关信息
     * json数据格式
     * {"status":0,"result":{"location":{"lng":113.009109696642,"lat":28.192963234242119},"precise":0,"confidence":50,"comprehension":0,"level":"NoClass"}}
     *
     * status：成功返回0
     * lng：经度
     * lat：纬度
     * precise：1为精确查找、0为模糊打点
     * confidence：误差范围
     * comprehension：地址精确程度，分值范围0-100，分值越大，服务对地址精确程度越高
     * level：地址类型
     */
    public static Map<String, String> getCoordinate(String address) {
        Map<String, String> fieldMap = new HashMap<>();
        if (address != null && !"".equals(address)) {
            // \s*为空字符串，如' '
            address = address.replaceAll("\\s*", "").replace("#", "栋");
            String param = "address=" + address + "&ak=" + AK;
            String json = loadJSON(URL + param);
            if (json != null && !"".equals(json)) {
                map = JSON.parseObject(json, Map.class);
                fieldMap = analyticalField(map);
            }
        }
        return fieldMap;
    }

    /**
     * json数据转化成Map
     */
    private static Map<String, String> analyticalField(Map<String, Object> map) {
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if ("result".equals(entry.getKey()) || "location".equals(entry.getKey())) {
                analyticalField(JSON.parseObject(entry.getValue().toString(), Map.class));
            } else {
                if ("lng".equals(entry.getKey()) || "lat".equals(entry.getKey())) {
                    DecimalFormat df = new DecimalFormat("#.######");
                    hashMap.put(entry.getKey(), df.format(entry.getValue()));
                } else {
                    hashMap.put(entry.getKey(), entry.getValue().toString());
                }
            }
        }
        return hashMap;
    }

/*    private static Map<String, String> analyticalField(Map<String, Object> map) {
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if ("content".equals(entry.getKey()) || "address_detail".equals(entry.getKey())
                    || "point".equals(entry.getKey())) {
                analyticalField(JSON.parseObject(entry.getValue().toString(), Map.class));
            } else {
                hashMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return hashMap;
    }*/

    /**
     * 请求百度地图链接，获取相关信息
     */
    public static String loadJSON(String urlStr) {
        StringBuilder builder = new StringBuilder();
        BufferedReader in = null;
        String inputLine = null;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
