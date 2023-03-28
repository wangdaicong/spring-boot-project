package cn.goitman.controller;

import cn.goitman.service.SearcherService;
import cn.goitman.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Nicky
 * @version 1.0
 * @className RegionController
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 测试接口
 * @date 2023/3/24 17:00
 */
@RestController
public class RegionController {

    @Autowired
    private SearcherService searcherService;

    @PostMapping("/getRegion")
    public String getRegion(HttpServletRequest httpServletRequest) {
        String ipAddr = IpUtil.getIpAddress(httpServletRequest);
        String region = searcherService.getRegion(ipAddr);
        return region;
    }

    @GetMapping("/getRegion2")
    public String getRegion2(@RequestParam String ipAddr) {
        String region = searcherService.getRegion(ipAddr);
        return region;
    }
}
