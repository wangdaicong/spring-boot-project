package cn.goitman.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nicky
 * @version 1.0
 * @className EnableSelectList
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 下拉列表注解；在有下拉列表数据的字段定义此注解，可选
 * @date 2021/9/22 14:56
 */
@Target(ElementType.FIELD) // 用于字段
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface EnableSelectList {
}
