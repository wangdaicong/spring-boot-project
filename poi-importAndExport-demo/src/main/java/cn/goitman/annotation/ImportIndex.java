package cn.goitman.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nicky
 * @version 1.0
 * @className ImportIndex
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 导入字段注解；必须在实体类上需导入数据的字段上定义此注解；
 *              定义字段索引属性时，必须与数据库相应字段的索引一致；
 *              实体类字段名必须与数据库字段名称一致，在本实例中未做转换逻辑
 * @date 2021/9/22 15:21
 */
@Target(ElementType.FIELD) // 用于字段
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface ImportIndex {

    // 索引，从0开始
    int index();

    // 导入设置set方法
    String useSetMethodName() default "";
}
