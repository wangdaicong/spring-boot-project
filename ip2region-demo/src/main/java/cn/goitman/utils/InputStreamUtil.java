package cn.goitman.utils;

import java.io.*;

/**
 * @author Nicky
 * @version 1.0
 * @className FileUtil
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 输入流工具
 * @date 2023/3/23 17:22
 */
public class InputStreamUtil {

    /**
    * 将输入流转化为字节数组
    */
    public static byte[] inputStreamToByteArray(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int num;
            while ((num = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, num);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}