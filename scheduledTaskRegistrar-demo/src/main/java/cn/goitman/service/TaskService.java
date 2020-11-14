package cn.goitman.service;

import cn.goitman.mapper.TaskMapper;
import cn.goitman.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className TaskService
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 业务逻辑层
 * @date 2020/10/28 15:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    public int insertTask(Task task) {
        return taskMapper.insertSelective(task);
    }

    public List<Task> getTaskListByStatus(Integer jobStatus) {
        Example example = new Example(Task.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobStatus", jobStatus);
        return taskMapper.selectByExample(example);
    }

    public Task findTaskByJobId(Integer id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    public boolean deleteTaskByJobId(Integer id) {
        return taskMapper.deleteByPrimaryKey(id) > 0 ? true : false;
    }

    public boolean updateTask(Task task) {
        return taskMapper.updateByPrimaryKeySelective(task) > 0 ? true : false;
    }

}
