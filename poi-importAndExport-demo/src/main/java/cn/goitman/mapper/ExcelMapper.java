package cn.goitman.mapper;

import cn.goitman.pojo.Student;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Nicky
 * @version 1.0
 * @className PoiMapper
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 数据层，继承MySqlMapper，获取批量插入方法
 * @date 2021/9/22 11:00
 */
@Repository
public interface ExcelMapper extends Mapper<Student>, MySqlMapper<Student> {
}
