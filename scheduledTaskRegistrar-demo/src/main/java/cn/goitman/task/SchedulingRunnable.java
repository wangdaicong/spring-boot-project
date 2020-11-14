package cn.goitman.task;

import cn.goitman.util.SpringContextUtils;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Nicky
 * @version 1.0
 * @className SchedulingRunnable
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 反射机制执行定时任务
 * @date 2020/10/27 16:05
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class SchedulingRunnable implements Runnable {
    private String beanName;
    private String methodName;
    private String params;

    @Override
    public void run() {
        log.info("定时任务开始执行 - bean：{}，方法：{}，参数：{}", beanName, methodName, params);
        long startTime = System.currentTimeMillis();

        try {
            Object target = SpringContextUtils.getBean(beanName);

            Method method = StringUtils.isEmpty(params) ?
                    target.getClass().getDeclaredMethod(methodName) :
                    target.getClass().getDeclaredMethod(methodName, String.class);

            ReflectionUtils.makeAccessible(method);

            if (StringUtils.isEmpty(params)) {
                method.invoke(target);
            } else {
                method.invoke(target, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("定时任务异常 - bean：{}，方法：{}，参数：{}", beanName, methodName, params);
        }
        long time = System.currentTimeMillis() - startTime;
        log.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{}", beanName, methodName, params, time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return beanName.equals(that.beanName) &&
                    methodName.equals(that.methodName) &&
                    that.params == null;
        }
        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                params.equals(that.params);
    }

    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }
        return Objects.hash(beanName, methodName, params);
    }
}
