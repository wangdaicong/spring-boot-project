package cn.goitman.controller;

import cn.goitman.enums.TaskStatus;
import cn.goitman.task.CronTaskRegistrar;
import cn.goitman.pojo.Task;
import cn.goitman.service.TaskService;
import cn.goitman.task.SchedulingRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Nicky
 * @version 1.0
 * @className TaskController
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 控制层类
 * @date 2020/10/28 16:41
 */
@RestController
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    /**
     * 添加任务
     */
    @PostMapping("/addTask")
    public String addTask(@Valid Task task) {
        if (taskService.insertTask(task) <= 0) {
            return "新增失败！";
        } else {
            if (task.getJobStatus().equals(TaskStatus.NORMAL.value())) {
                SchedulingRunnable runnable = new SchedulingRunnable(task.getBeanName(), task.getMethodName(), task.getMethodParams());
                cronTaskRegistrar.addCronTask(runnable, task.getCronExpression());
            }
        }
        return "新增成功！";
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Integer id) {
        Task task = taskService.findTaskByJobId(id);
        if (!taskService.deleteTaskByJobId(id)) {
            return "删除失败！";
        } else {
            if (task.getJobStatus().equals(TaskStatus.NORMAL.value())) {
                SchedulingRunnable runnable = new SchedulingRunnable(task.getBeanName(), task.getMethodName(), task.getMethodParams());
                cronTaskRegistrar.removeCronTask(runnable);
            }
        }
        return "删除成功！";
    }

    /**
     * 修改任务
     */
    @PostMapping("/updateTask")
    public String updateTask(@Valid Task taskNew) {
        Task taskOld = taskService.findTaskByJobId(taskNew.getId());
        if (taskService.updateTask(taskNew)) {
            // 先取消原有的定时任务，并删除缓存
            if (taskOld.getJobStatus().equals(TaskStatus.NORMAL.value())) {
                // 链式编程，使用了lombok的注解@Accessors
                SchedulingRunnable runnable = new SchedulingRunnable()
                        .setBeanName(taskOld.getBeanName())
                        .setMethodName(taskOld.getMethodName())
                        .setParams(taskOld.getMethodParams());
                cronTaskRegistrar.removeCronTask(runnable);
            }
            // 新增定时任务，并添加到缓存
            if (taskNew.getJobStatus().equals(TaskStatus.NORMAL.value())) {
                SchedulingRunnable runnable = new SchedulingRunnable(taskNew.getBeanName(), taskNew.getMethodName(), taskNew.getMethodParams());
                cronTaskRegistrar.addCronTask(runnable, taskNew.getCronExpression());
            }
        } else {
            return "更新失败！";
        }
        return "更新成功！";
    }

    /**
     * 修改任务状态
     */
    @GetMapping("/updateTaskStatus/{id}")
    public String updateTaskStatus(@PathVariable Integer id) {
        Task task = taskService.findTaskByJobId(id);
        // 如原先是启动状态，便设置为停止，并从缓存中删除，反之亦然
        if (task.getJobStatus().equals(TaskStatus.NORMAL.value())) {
            SchedulingRunnable runnable = new SchedulingRunnable(task.getBeanName(), task.getMethodName(), task.getMethodParams());
            cronTaskRegistrar.removeCronTask(runnable);
            task.setJobStatus(0);
            return taskService.updateTask(task) ? "修改成功！" : "修改失败！";
        } else {
            SchedulingRunnable runnable = new SchedulingRunnable(task.getBeanName(), task.getMethodName(), task.getMethodParams());
            cronTaskRegistrar.addCronTask(runnable, task.getCronExpression());
            task.setJobStatus(1);
            return taskService.updateTask(task) ? "修改成功！" : "修改失败！";
        }
    }
}
