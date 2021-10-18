package cn.goitman.pojo;

import cn.goitman.annotation.EnableExport;
import cn.goitman.annotation.EnableExportField;
import cn.goitman.annotation.EnableSelectList;
import cn.goitman.annotation.ImportIndex;
import cn.goitman.enums.ColorEnum;

/**
 * @author Nicky
 * @version 1.0
 * @className Student
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 实体类，excel表映射
 * @date 2021/9/22 15:55
 */
@EnableExport(fileName = "学生表")
public class Student {

    @ImportIndex(index = 0)
    private String sno;

    @EnableExportField(colName = "姓名", colWidth = 90)
    @ImportIndex(index = 1)
    private String sname;

    @EnableExportField(colName = "年龄", colWidth = 90, cellColor = ColorEnum.YELLOW)
    @ImportIndex(index = 2)
    private String sage;

//    @EnableSelectList
    @EnableExportField(colName = "性别", colWidth = 90)
    @ImportIndex(index = 3, useSetMethodName = "setSex")
    private String ssex;

    public Student() {
    }

    public Student(String sno, String sname, String sage, String ssex) {
        this.sno = sno;
        this.sname = sname;
        this.sage = sage;
        this.ssex = ssex;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSage() {
        return sage;
    }

    public void setSage(String sage) {
        this.sage = sage;
    }

    public String getSsex() {
        return ssex;
    }

    public void setSsex(String ssex) {
        this.ssex = ssex;
    }

    public void setSex(String sex) {
        if (("男").equals(sex)) {
            this.ssex = "3";
        }
        if (("女").equals(sex)) {
            this.ssex = "4";
        }
    }
}
