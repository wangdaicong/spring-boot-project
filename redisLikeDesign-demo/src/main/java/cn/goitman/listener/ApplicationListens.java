package cn.goitman.listener;

import cn.goitman.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Nicky
 * @version 1.0
 * @className ApplicationListens
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 监听程序启动与关闭，回调钩子
 *              CommandLineRunner接口：当应用启动成功后的回调
 *              DisposableBean接口：当应用正要被销毁前的回调
 * @date 2022/5/12 15:57
 */
//@Component
public class ApplicationListens implements CommandLineRunner, DisposableBean {

    private static Logger log = LoggerFactory.getLogger(ApplicationListens.class);

    @Autowired
    private LikeService likeService;

    /**
     * 启动后回调
     */
    @Override
    public void run(String... args) throws Exception {

    }

    /**
     * 关闭前回调
     */
    @Override
    public void destroy() throws Exception {
        log.info("程序关闭，钩子回调.........开始.........");
        likeService.savaLikeData2DB();
        likeService.saveArticleLikeCount2DB();
        log.info("程序关闭，钩子回调.........结束.........");
    }
}
