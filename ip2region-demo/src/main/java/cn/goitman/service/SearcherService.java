package cn.goitman.service;

import cn.goitman.utils.InputStreamUtil;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author Nicky
 * @version 1.0
 * @className SearcherFile
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 解析ip地址属性
 * @date 2023/3/23 17:21
 */
@Service
public class SearcherService {
    public String getRegion(String ip) {
        // jar包也能获取ip2region.xdb文件
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("ip2region.xdb");
        byte[] bytes = InputStreamUtil.inputStreamToByteArray(in);

        try {
            Searcher searcher = Searcher.newWithBuffer(bytes);

            long sTime = System.nanoTime();
            // 中国|0|上海|上海市|联通；美国|0|犹他|盐湖城|0
            String regionInfo = searcher.search(ip);
            String region = getCityInfo(regionInfo);
            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
            System.out.printf("{IP属地 : %s, ip: %s, 耗时: %d 纳秒}\n", region, ip, cost);
            return region;
        } catch (Exception e) {
            System.out.printf("IP地址异常 (%s) : %s\n", ip, e);
            return null;
        }
    }

    /**
     * 解析城市信息，国内显示城市名，国外显示国家名
     */
    private String getCityInfo(String regionInfo) {
        if (!StringUtils.isEmpty(regionInfo)) {
            String[] cityArr = regionInfo.replace("|0", "").replace("0|", "").split("\\|");
            if (cityArr.length > 0) {
                if ("内网ip".equalsIgnoreCase(cityArr[0])) {
                    return "内网IP";
                }
                if ("中国".equals(cityArr[0])) {
                    return cityArr[1];
                }
                return cityArr[0];
            }
        }
        return "未知IP";
    }
}