package com.scheduledtask.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Nicky
 * @version 1.0
 * @className SpringContextUtils
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 获取当前上下文对象工具类
 * @date 2020/10/27 16:07
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    // 应用上下文对象
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * @method  getBean
     * @description  获取当前上下文对象
     * @param  [name]
     * @return  java.lang.Object
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
