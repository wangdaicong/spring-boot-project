package cn.goitman.task;

import cn.goitman.pojo.Task;
import cn.goitman.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className TaskRunner
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 初始化定时任务类
 *              如有多个组件实现了CommandLineRunner接口来实现启动加载功能，Order注解可实现先后加载顺序
 * @date 2020/10/28 14:33
 */
@Slf4j
@Component
// @Order(value = 1)
public class TaskRunner implements CommandLineRunner {

    @Autowired
    private TaskService taskService;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Override
    public void run(String... args) {
        // 重新开启线程，避免影响主程序的启动
        new Thread() {
            public void run() {
                // 查找任务状态为正常的任务数据
                List<Task> taskList = taskService.getTaskListByStatus(1);

                if (!CollectionUtils.isEmpty(taskList)) {
                    for (Task task : taskList) {
                        // 执行定时任务
                        SchedulingRunnable runnable = new SchedulingRunnable(task.getBeanName(), task.getMethodName(), task.getMethodParams());
                        // 注册任务数据
                        cronTaskRegistrar.addCronTask(runnable, task.getCronExpression());
                    }
                    log.info("定时任务加载完毕......");
                }
            }
        }.start();
    }
}
