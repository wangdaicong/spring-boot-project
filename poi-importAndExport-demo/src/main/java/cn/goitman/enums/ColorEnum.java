package cn.goitman.enums;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author Nicky
 * @version 1.0
 * @className ColorEnum
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 设置颜色枚举
 * @date 2021/9/22 16:05
 */
public enum ColorEnum {

    RED("红色", HSSFColor.RED.index),
    GREEN("绿色", HSSFColor.GREEN.index),
    BLANK("白色", HSSFColor.BLACK.index),
    YELLOW("黄色", HSSFColor.YELLOW.index),
    BLUE("蓝色", HSSFColor.BLUE.index);

    private String name;
    private Short index;

    ColorEnum(String name, Short index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getIndex() {
        return index;
    }

    public void setIndex(Short index) {
        this.index = index;
    }
}
