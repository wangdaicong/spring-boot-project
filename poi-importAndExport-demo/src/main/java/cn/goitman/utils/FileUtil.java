package cn.goitman.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Nicky
 * @version 1.0
 * @className ExcelService
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 文件工具类
 * @date 2021/9/22 16:09
 */
// 注入容器，获取配置文件数据
@Component
public class FileUtil {

    private static String filePath;

    @Value("${file.filePath}")
    public void setFilePath(String filePath) {
        FileUtil.filePath = filePath;
    }

    /**
     * 保存文件到本地， 保存后的文件路径（绝对路径）
     */
    public static String saveFileToLocal(MultipartFile file) {
        Date date = new Date();
        String str = "yyyMM";
        SimpleDateFormat sdf = new SimpleDateFormat(str);

        // 按年月分文件夹
        File file1 = new File(filePath + sdf.format(date) + File.separator);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        // 获取文件后缀名
        String suffixFileName = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String path = file1.getPath() + File.separator + uuid + suffixFileName;
        try {
            file.transferTo(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
