package cn.goitman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Nicky
 * @version 1.0
 * @className PoiApplication
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 启动类
 * @date 2021/9/22 10:57
 */
@SpringBootApplication
@MapperScan("cn.goitman.mapper") // 指定扫描的Mapper类的包的路径，在每个Mapper类上添加注解@Mapper
public class ExcelApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExcelApplication.class,args);
    }
}
