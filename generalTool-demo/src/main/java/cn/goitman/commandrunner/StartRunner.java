package cn.goitman.commandrunner;

import cn.goitman.mapper.BallDao;
import cn.goitman.mapper.BallTwoDao;
import cn.goitman.pojo.Period;
import cn.goitman.service.ThreadPoolService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className StartRunner
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 测试
 * @date 2022/7/12 16:48
 */
@Component
@Order(0)
public class StartRunner implements CommandLineRunner {

    @Autowired
    private ThreadPoolService threadPoolService;

    @Override
    public void run(String... args) throws Exception {
        List<Period> list = Lists.newArrayList();

        int size = 100001;
        for (int i = 1; i < size; i++) {
            Period period = new Period(i + "", i + "", i + "", i + "", i + "", i + "", i + "");
            list.add(period);
        }

//        threadPoolService.threadMethod(list, BallDao.class, (data, BallDao) -> BallDao.insertPeriod(data));
        threadPoolService.threadMethod(list, BallTwoDao.class, (data, BallTwoDao) -> BallTwoDao.deletePeriod(data));
    }
}
