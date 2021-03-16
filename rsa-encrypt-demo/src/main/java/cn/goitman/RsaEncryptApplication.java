package cn.goitman;

import cn.shuibo.annotation.EnableSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Nicky
 * @version 1.0
 * @className RsaEncryptApplication
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 启动引导类
 * @date 2020/11/2 16:31
 */
@SpringBootApplication
@EnableSecurity // 启用加解密注解
public class RsaEncryptApplication {
    public static void main(String[] args) {
        SpringApplication.run(RsaEncryptApplication.class, args);
    }
}
