package cn.goitman.controller;

import cn.goitman.annotation.Restrict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nicky
 * @version 1.0
 * @className RestrictController
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 测试接口
 * @date 2022/3/16 10:07
 */
@RestController
public class RestrictController {

    @GetMapping("/restrict1")
    @Restrict(key = "restrict1", limitedNumber = 1, msg = "当前排队人数较多，请稍后再试！")
    public String restrict1() {
        return "OK";
    }

    @GetMapping("/restrict2")
    @Restrict(key = "restrict2", limitedNumber = 2)
    public String restrict2() {
        return "OK";
    }
}
