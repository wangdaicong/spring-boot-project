package cn.goitman.scheduling;

import cn.goitman.service.LikeService;
import cn.goitman.task.LikeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nicky
 * @version 1.0
 * @className LikeScheduling
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Scheduled 执行类
 * @date 2022/5/13 15:55
 */
//@Component
public class LikeScheduling {

    private static Logger log = LoggerFactory.getLogger(LikeScheduling.class);

    @Autowired
    private LikeService likeService;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void likeCron() {
        log.info("Scheduled 定时任务.........开始.........");
        likeService.savaLikeData2DB();
        likeService.saveArticleLikeCount2DB();
        log.info("Scheduled 定时任务.........结束.........");
    }
}
