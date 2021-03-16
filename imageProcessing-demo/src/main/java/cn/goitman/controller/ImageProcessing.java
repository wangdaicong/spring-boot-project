package cn.goitman.controller;

import cn.goitman.util.FileUtils;
import com.alibaba.fastjson.JSON;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Nicky
 * @version 1.0
 * @className ImageProcessing
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 图片处理
 * @date 2021/3/9 10:56
 */
@RestController
public class ImageProcessing {

    @RequestMapping("/process")
    public String imagePro(MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();

        String filename = null;
        String url = "http://127.0.0.1:8080/img/";
        String localPath = null;
        String srcPath = null;
        String suffixName = null;

        String type = file.getContentType();
        if (type.equals("image/jpeg") || type.equals("image/jpg") || type.equals("image/png") || type.equals("image/gif")) {
            filename = file.getOriginalFilename();
            suffixName = filename.substring(filename.lastIndexOf("."));
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            localPath = this.getClass().getResource("/").getPath() + "static/img/";
            srcPath = localPath + uuidName;

            if (FileUtils.upload(file, localPath, uuidName)) {
                map.put("srcPath", url + uuidName);
                map.put("srcMsg", "原图大小为：" + String.format("%.0f", file.getSize() / 1024f) + " KB");
            }
        }

//        thumbnailImg(map, 200, 300, url, localPath, srcPath, suffixName);
//        thumbnailImg2(map, 200, 300, url, localPath, srcPath, suffixName);
//        proportionImg(map, 0.5, url, localPath, srcPath, suffixName);
//        rotatingImg(map, 200, 300, 90, url, localPath, srcPath, suffixName);
//        compressionImg(map, 1, 1, url, localPath, srcPath, suffixName);
//        watermarkImg(map, 500, 500,"1.jpg",0.5f,0.8, url, localPath, srcPath, suffixName);
//        tailoringImg(map, 300, 300, url, localPath, srcPath, suffixName);
//        conversionImg(map, "gif", url, localPath, srcPath, suffixName);
//        outputStream(map, "gif", url, localPath, srcPath, suffixName);
        bufferedImg(map, "bmp", url, localPath, srcPath, suffixName);
        return JSON.toJSONString(map);
    }

    /**
     * @method thumbnailImg
     * @description 按指定大小缩放图片（遵循原图高宽比例）
     * @param [map, wdith 宽度, heigth 高度, url 请求地址, localPath 本地项目地址, srcPath 原图片地址, suffixName 文件后缀名]
     * @return void
     */
    private void thumbnailImg(Map<String, Object> map, int wdith, int heigth, String url, String localPath, String srcPath, String suffixName) {
        try {
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            // of()可设置为图片目录地址，toFile()可只设为图片转换后的目录地址
            Thumbnails.of(srcPath)
                    .size(wdith, heigth)
                    .toFile(localPath + uuidName);
            File file = new File(localPath + uuidName);
            BufferedImage bin = ImageIO.read(file);
            map.put("tbgPath", url + uuidName);
            map.put("tbgMsg", "遵循原图缩略大小为：" + String.format("%.0f", file.length() / 1024f) + " KB; " + "比例大小为：" + bin.getWidth() + "*" + bin.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @method thumbnailImg2
     * @description 按指定大小缩放图片（不遵循原图比例）
     * @param [map, wdith, heigth, url, localPath, srcPath , suffixName]
     * @return void
     */
    private void thumbnailImg2(Map<String, Object> map, int wdith, int heigth, String url, String localPath, String srcPath, String suffixName) {
        try {
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            // keepAspectRatio值为false，默认为true
            Thumbnails.of(srcPath)
                    .size(wdith, heigth)
                    .keepAspectRatio(false)
                    .toFile(localPath + uuidName);
            File file = new File(localPath + uuidName);
            // 图像缓存区类
            BufferedImage bin = ImageIO.read(file);
            map.put("ntbgPath", url + uuidName);
            map.put("ntbgMsg", "不遵循原图缩略大小为：" + String.format("%.0f", file.length() / 1024f) + " KB; " + "比例大小为：" + bin.getWidth() + "*" + bin.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @method  proportionImg
     * @description  按比例率缩放图片
     * @param  [map, percentag 比例值, url, localPath, srcPath, suffixName]
     * @return  void
     */
    private void proportionImg(Map<String, Object> map, double percentag, String url, String localPath, String srcPath, String suffixName) {
        try {
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            // scale 取值范围：大于1：放大，反之缩小；等于1：比例不变，压缩图片大小；等同于百分比)
            Thumbnails.of(srcPath)
                    .scale(percentag)
                    .toFile(localPath + uuidName);
            map.put("pptPath", url + uuidName);
            map.put("pptMsg", "比例图大小为：" + String.format("%.0f", new File(localPath + uuidName).length() / 1024f) + " KB; " + "比例值为：" + percentag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @method rotatingImg
     * @description 旋转图片
     * @param [map, wdith, heigth, angle 角度, url, localPath, srcPath, suffixName]
     * @return void
     */
    private void rotatingImg(Map<String, Object> map, int wdith, int heigth, int angle, String url, String localPath, String srcPath, String suffixName) {
        try {
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            // rotate角度：正数顺时针旋转，反之亦然
            Thumbnails.of(srcPath)
                    .size(wdith, heigth)
                    .rotate(angle)
                    .toFile(localPath + uuidName);
            map.put("rtPath", url + uuidName);
            map.put("rtMsg", "旋转角度为：" + angle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @method compressionImg
     * @description 压缩图片文件大小
     * @param [map, percentag 比例值, compressValue 压缩值, url, localPath, srcPath , suffixName]
     * @return void
     */
    private void compressionImg(Map<String, Object> map, double percentag, double compressValue, String url, String localPath, String srcPath, String suffixName) {
        try {
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            // outputQuality 取值范围：0.0-1.0之间，等于1质量最高，等同于百分比，文件大小变大
            Thumbnails.of(srcPath)
                    .scale(percentag)
                    .outputQuality(compressValue)
                    .toFile(localPath + uuidName);
            map.put("cpPath", url + uuidName);
            map.put("cpMsg", "压缩图大小为：" + String.format("%.0f", new File(localPath + uuidName).length() / 1024f) + " KB; " + "压缩值为：" + compressValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @method  watermarkImg
     * @description  添加水印
     * @param  [map, wdith, heigth, fileName 图片名称, transparency 透明度, compressValue 压缩值, url, localPath, srcPath, suffixName]
     * @return  void
     */
    private void watermarkImg(Map<String, Object> map, int wdith, int heigth, String fileName, float transparency, double compressValue, String url, String localPath, String srcPath, String suffixName) {
        try {
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            // 读取水印图片
            BufferedImage read = ImageIO.read(new File(localPath + fileName));
            /*
             * watermark(位置，水印图，透明度)：Positions.BOTTOM_RIGHT表示在右下角，有9个位置枚举可选
             * transparency 取值范围：0.0-1.0之间，1为不透明
             */
            Thumbnails.of(srcPath)
                    .size(wdith, heigth)
                    .watermark(Positions.BOTTOM_RIGHT, read, transparency)
                    .outputQuality(compressValue).toFile(localPath + uuidName);
            map.put("wmPath", url + uuidName);
            map.put("wmMsg", "添加水印成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @method  tailoringImg
     * @description  图片裁剪
     * @param  [map, wdith, heigth, url, localPath, srcPath, suffixName]
     * @return  void
     */
    private void tailoringImg(Map<String, Object> map, int wdith, int heigth, String url, String localPath, String srcPath, String suffixName) {
        try {
            String uuidName = UUID.randomUUID().toString().replace("-", "") + suffixName;
            /*
             * sourceRegion(位置，裁剪宽度，裁剪高度)
             * 位置：Positions.CENTER 表示在中间，有9个位置枚举可选，也可用两个像素值定位
             */
            Thumbnails.of(srcPath)
//                    .sourceRegion(Positions.CENTER, wdith, heigth)
                    .sourceRegion(0,0, wdith, heigth)
                    .size(wdith, heigth)
                    .toFile(localPath + uuidName);
            map.put("tiPath", url + uuidName);
            map.put("tiMsg", "图片裁剪成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * @method  conversionImg
    * @description 图片格式转换
    * @param  [map, format 图片格式, url, localPath, srcPath, suffixName]
    * @return  void
    */
    private void conversionImg(Map<String, Object> map, String format, String url, String localPath, String srcPath, String suffixName) {
        try {
            String formatPath = srcPath.substring(0, srcPath.lastIndexOf(".")) + "." + format;
            String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1, srcPath.lastIndexOf(".")) + "." + format;
            // outputFormat： 支持bmp,jpg,png,gif,jpeg格式
            Thumbnails.of(srcPath)
                    .scale(0.5f)
                    .outputFormat(format)
                    .toFile(formatPath);
            map.put("ciPath", url + fileName);
            map.put("ciMsg", "格式为：" + format + "    图片大小为：" + String.format("%.0f", new File(formatPath).length() / 1024f) + " KB; ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * @method  outputStream
    * @description 把图片输出至输出流
     * @param  [map, format, url, localPath, srcPath, suffixName]
    * @return  void
    */
    private void outputStream(Map<String, Object> map, String format, String url, String localPath, String srcPath, String suffixName) {
        try {
            String formatPath = srcPath.substring(0, srcPath.lastIndexOf(".")) + "." + format;
            String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1, srcPath.lastIndexOf(".")) + "." + format;
            OutputStream os = new FileOutputStream(formatPath);

            Thumbnails.of(srcPath).size(300, 400).outputFormat(format).toOutputStream(os);
            map.put("osPath", url + fileName);
            map.put("osMsg", "输出文件流成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * @method  bufferedImg
    * @description  输出图片缓冲流
    * @param  [map, format, url, localPath, srcPath, suffixName]
    * @return  void
    */
    private void bufferedImg(Map<String, Object> map, String format,String url, String localPath, String srcPath, String suffixName) {
        try {
            String formatPath = srcPath.substring(0, srcPath.lastIndexOf(".")) + "." + format;
            String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1, srcPath.lastIndexOf(".")) + "." + format;
            OutputStream os = new FileOutputStream(formatPath);

            BufferedImage bi = Thumbnails.of(srcPath).size(300, 400).asBufferedImage();
            ImageIO.write(bi, format, os);
            map.put("biPath", url + fileName);
            map.put("biMsg", "输出图片缓冲流成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}