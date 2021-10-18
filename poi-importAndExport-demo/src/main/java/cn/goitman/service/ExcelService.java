package cn.goitman.service;

import cn.goitman.mapper.ExcelMapper;
import cn.goitman.pojo.Student;
import cn.goitman.utils.ExcelUtils;
import cn.goitman.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.goitman.utils.ExcelUtils.ALL_SELECT_LIST_MAP;

/**
 * @author Nicky
 * @version 1.0
 * @className ExcelService
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 业务逻辑层
 * @date 2021/9/22 16:09
 */
@Service
@Transactional
public class ExcelService {

    @Autowired
    private ExcelMapper excelMapper;

    public boolean uploadFile(MultipartFile file) {
        String path = FileUtil.saveFileToLocal(file);
        File fileDirPath = new File(path);

        List<Student> students = (List<Student>) ExcelUtils.parseExcelToList(fileDirPath, Student.class);
        return excelMapper.insertList(students) > 0;
    }

    public boolean uploadFileSelectList(MultipartFile file) throws FileNotFoundException {
/*        // 预存数据，配合EnableSelectList注解使用
        Map<String, String> selsctList = new HashMap<>();
        selsctList.put("1","男");
        selsctList.put("2","女");
        ALL_SELECT_LIST_MAP.put(3,selsctList);*/

        String path = FileUtil.saveFileToLocal(file);
        FileInputStream inputStream = new FileInputStream(path);

        List<Student> students = (List<Student>) ExcelUtils.parseExcelToList(inputStream, Student.class);
        return excelMapper.insertList(students) > 0;
    }

    public void downloadFile(HttpServletResponse response) {
        List<Student> students = excelMapper.selectAll();
        ExcelUtils.exportExcel(response, students, Student.class, null, null);
    }
}
