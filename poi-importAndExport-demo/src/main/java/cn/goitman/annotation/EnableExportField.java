package cn.goitman.annotation;

import cn.goitman.enums.ColorEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nicky
 * @version 1.0
 * @className EnableExportField
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 允许导出字段注解；在实体类上，需导出数据的字段上定义此注解，可选
 * @date 2021/9/22 11:10
 */
@Target(ElementType.FIELD)  // 用于字段上
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface EnableExportField {
    // 设置宽度，默认100像素
    int colWidth() default 100;

    // 列名称
    String colName();

    // 导出设置get方法
    String useGetMethod() default "";

    // 背景颜色，默认蓝色
    ColorEnum cellColor() default ColorEnum.BLUE;
}
