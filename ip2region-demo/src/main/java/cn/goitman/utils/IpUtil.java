package cn.goitman.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Nicky
 * @version 1.0
 * @className IpUtil
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 解析IP地址工具
 * @date 2023/3/23 16:45
 */
public class IpUtil {

    private static Logger log = LoggerFactory.getLogger(IpUtil.class);

    private static final String UNKNOWN = "unknown";

    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        try {
            // k8s将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
            ip = request.getHeader("X-Original-Forwarded-For");
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            // 通过nginx获取ip
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            // 通过Apache代理获取ip
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            // 通过WebLogic获取ip
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            // 通过负载均衡获取IP地址（HTTP_CLIENT_IP、HTTP_X_FORWARDED_FOR）
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            // 通过Nginx获取ip（Nginx中的另一个变量，内容就是请求中X-Forwarded-For的信息）
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            //兼容集群获取ip
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                // 客户端和服务器为同一台机器时，获取的地址为IPV6格式："0:0:0:0:0:0:0:1"
                if ("127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip) || "localhost".equalsIgnoreCase(ip)) {
                    //根据网卡取本机配置的IP
                    InetAddress iNet = null;
                    try {
                        iNet = InetAddress.getLocalHost();
                        ip = iNet.getHostAddress();
                    } catch (UnknownHostException e) {
                        log.error("根据网卡获取IP地址异常: ", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取IP地址异常 ", e);
        }
        //使用代理，则获取第一个IP地址
        if (!StringUtils.isEmpty(ip) && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }
}