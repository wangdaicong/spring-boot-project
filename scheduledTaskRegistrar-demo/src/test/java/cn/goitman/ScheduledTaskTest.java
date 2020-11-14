package cn.goitman;

import cn.goitman.controller.TaskController;
import cn.goitman.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Nicky
 * @version 1.0
 * @className ScheduledTaskTest
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 测试类
 * @date 2020/10/29 15:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduledTaskApplication.class)
public class ScheduledTaskTest {

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskService taskService;

    @Test
    public void testTaskController(){
/*      Task task = new Task();
        task.setBeanName("TaskOne");
        task.setMethodName("taskWithParams");
        task.setMethodParams("111");
        task.setCronExpression("0/5 * * * * ?");
        task.setJobStatus(1);
        task.setRemark("111");
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        String msg = taskController.addTask(task);*/


//        String msg = taskController.deleteTask(2);


/*      Task task = new Task();
        task.setId(50);
        task.setBeanName("TaskOne");
        task.setMethodName("taskNoParams");
        task.setMethodParams("");
        task.setCronExpression("0/5 * * * * ?");
        task.setJobStatus(1);
        task.setRemark("111");
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        String msg = taskController.updateTask(task);*/

        String msg = taskController.updateTaskStatus(50);
        System.out.println(msg);
    }
}
