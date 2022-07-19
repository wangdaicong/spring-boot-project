package cn.goitman.mapper;

import cn.goitman.pojo.Period;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className BallDao
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 数据层
 * @date 2022/4/19 16:09
 */
@Mapper
public interface BallDao {

    int insertPeriod(List<Period> list);

}
