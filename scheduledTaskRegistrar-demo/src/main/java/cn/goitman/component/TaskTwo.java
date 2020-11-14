package cn.goitman.component;

import org.springframework.stereotype.Component;

/**
 * @author Nicky
 * @version 1.0
 * @className TaskTwo
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 定时任务业务逻辑类
 *              Component中的value对应数据库的beanName字段
 *              方法名对应数据库的methodName字段
 *              参数对应数据库的methodParams字段
 * @date 2020/10/30 16:16
 */
@Component("TaskTwo")
public class TaskTwo {
    public void taskWithParams(String params) {
//        System.out.println("执行有参任务：" + params);
    }

    public void taskNoParams() {
//        System.out.println("执行无参任务");
    }
}
