package cn.goitman.controller;

import cn.goitman.pojo.Student;
import cn.goitman.service.ExcelService;
import cn.goitman.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className ExcelController
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 控制层
 * @date 2021/9/22 10:53
 */
@RestController
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    /**
    * @method  uploadFile
    * @description 常规导入
    * @param  [file]
    * @return  boolean
    */
    @PostMapping("/uploadFile")
    public boolean uploadFile(@RequestParam("fileName") MultipartFile file) {
        return excelService.uploadFile(file);
    }

    /**
    * @method  uploadFileSelectList
    * @description 演示EnableSelectList注解、ImportIndex注解useSetMethodName属性的使用
    * @param  [file]
    * @return  boolean
    */
    @PostMapping("/uploadFileSelectList")
    public boolean uploadFileSelectList(@RequestParam("fileName") MultipartFile file) {
        try {
            return excelService.uploadFileSelectList(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
    * @method  downloadFile
    * @description 常规导出
    * @param  [response]
    * @return  void
    */
    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response) {
        excelService.downloadFile(response);
    }
}
