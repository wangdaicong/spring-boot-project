package com.scheduledtask.mapper;

import com.scheduledtask.pojo.Task;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Nicky
 * @version 1.0
 * @className TaskMapper
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 数据层
 * @date 2020/10/28 11:10
 */
@Repository
public interface TaskMapper extends Mapper<Task> {

}
