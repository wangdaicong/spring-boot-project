package cn.goitman.annotation;

import cn.goitman.enums.ColorEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nicky
 * @version 1.0
 * @className EnableExport
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 允许导出类注解；实体类上必须定义此注解，因工具类会判断当前类是否为允许导出
 * @date 2021/9/22 10:48
 */
@Target(ElementType.TYPE) // 用于类
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface EnableExport {

    // 标题/文件名
    String fileName();

    // 背景颜色，默认蓝色
    ColorEnum cellColor() default ColorEnum.BLUE;
}
