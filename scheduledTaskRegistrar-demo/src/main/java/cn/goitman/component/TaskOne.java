package cn.goitman.component;

import org.springframework.stereotype.Component;

/**
 * @author Nicky
 * @version 1.0
 * @className TaskOne
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 定时任务业务逻辑类
 *              Component中的value对应数据库的beanName字段
 *              方法名对应数据库的methodName字段
 *              参数对应数据库的methodParams字段
 * @date 2020/10/30 16:16
 */
@Component("TaskOne")
public class TaskOne {
    public void taskWithParams(String params) {
        /*
        *   此处写有参定时任务的业务逻辑
        */
    }

    public void taskNoParams() {
        /*
         *   此处写无参定时任务的业务逻辑
         */
    }
}
