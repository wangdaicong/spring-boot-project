package cn.goitman.task;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nicky
 * @version 1.0
 * @className CronTaskRegistrar
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 定时任务注册类
 * @date 2020/10/27 17:23
 */
@Component
public class CronTaskRegistrar implements DisposableBean {

    /**
     * 在多线程下，使用并发集合做为缓存，初始化容量16
     */
    private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

    // 注入定时任务接口
    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * @param [task 定时任务, cronExpression cron表达式]
     * @return void
     * @method addCronTask
     * @description 添加定时任务
     */
    public void addCronTask(Runnable task, String cronExpression) {
        addCronTask(new CronTask(task, cronExpression));
    }

    /**  
    * @method  addCronTask
    * @description  注册定时任务，并将任务加入到缓存中
    * @param  [cronTask] 定时任务对象
    * @return  void
    */ 
    public void addCronTask(CronTask cronTask) {
        if (cronTask != null) {
            Runnable task = cronTask.getRunnable();
            if (scheduledTasks.containsKey(task)) {
                removeCronTask(task);
            }
            scheduledTasks.put(task, scheduleCronTask(cronTask));
        }
    }

    /**  
    * @method  removeCronTask
    * @description  取消定时任务，并将缓存中的任务记录删除
    * @param  [task] 线程对象
    * @return  void
    */ 
    public void removeCronTask(Runnable task) {
        ScheduledTask scheduledTask = scheduledTasks.remove(task);
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }
    }

    /**  
    * @method  scheduleCronTask
    * @description  调用线程池
    * @param  [cronTask] 定时任务对象
    * @return  com.scheduledtask.task.ScheduledTask
    */ 
    public ScheduledTask scheduleCronTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        // 指定一个触发器执行定时任务，并返回执行结果
        scheduledTask.future = taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }

    /**  
    * @method  destroy
    * @description  销毁所有定时任务，并将缓存清除
    * @return  void
    */ 
    @Override
    public void destroy() throws Exception {
        for (ScheduledTask task : scheduledTasks.values()) {
            task.cancel();
        }
        scheduledTasks.clear();
    }
}