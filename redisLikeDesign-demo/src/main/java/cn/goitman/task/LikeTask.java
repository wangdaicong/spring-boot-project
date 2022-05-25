package cn.goitman.task;

import cn.goitman.listener.ApplicationListens;
import cn.goitman.service.LikeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Nicky
 * @version 1.0
 * @className LikeTask
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Quartz 执行类
 * @date 2022/5/23 11:26
 */
public class LikeTask extends QuartzJobBean {

    private static Logger log = LoggerFactory.getLogger(LikeTask.class);

    @Autowired
    LikeService likeService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Quartz定时任务.........开始.........");
        likeService.savaLikeData2DB();
        likeService.saveArticleLikeCount2DB();
        log.info("Quartz定时任务.........结束.........");
    }
}
