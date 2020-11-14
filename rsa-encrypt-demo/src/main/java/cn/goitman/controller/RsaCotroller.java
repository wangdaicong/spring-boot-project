package cn.goitman.controller;

import cn.goitman.util.CreateRsaSecrteKeyUtil;
import cn.shuibo.annotation.Decrypt;
import cn.shuibo.annotation.Encrypt;
import com.alibaba.fastjson.JSON;
import cn.goitman.pojo.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nicky
 * @version 1.0
 * @className RsaCotroller
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description
 * @date 2020/11/2 16:43
 */
@RestController
public class RsaCotroller {

    @Value("${rsa.encrypt.privateKey}")
    private String privateKey;

    @Value("${rsa.encrypt.publicKey}")
    private String publicKey;

    /**
     * 注解加密
     */
    @Encrypt
    @GetMapping("/encrypt")
    public String encrypt() {
        Consumer consumer = new Consumer("注解加密用户", "注解加密密码");
        return consumer.toString();
    }

    /**
     * 注解解密
     */
    @Decrypt
    @PostMapping("/decrypt")
    public String decrypt(@RequestBody Consumer consumer) {
        return consumer.toString();
    }

    /**
     * 手工加密
     */
    @GetMapping("/encryption")
    public String encryption() {
        Consumer consumer = new Consumer("手工加密用户", "手工加密密码");
        // 因前端传送的是JSON数据，所以需先将对象转为JSON后，再加密
        return CreateRsaSecrteKeyUtil.encryptRSADate(publicKey, JSON.toJSONString(consumer));
    }

    /**
     * 手动解密
     */
    @PostMapping("/decryption")
    public String decryption(@RequestBody String str) {
        Consumer consumer = JSON.parseObject(CreateRsaSecrteKeyUtil.decryptRSADate(privateKey, str), Consumer.class);
        return consumer.toString();
    }

    /**
     * 手工加密加签
     */
    @GetMapping("/signature")
    public String signature() {
        Consumer consumer = new Consumer("手工加密加签", "手工加密加签");
        // 因前端传送的是JSON数据，所以需先将对象转为JSON后，再加密
        String encryptDate = CreateRsaSecrteKeyUtil.encryptRSADate(publicKey, JSON.toJSONString(consumer));
        byte[] bytes = encryptDate.getBytes();
        String sign = CreateRsaSecrteKeyUtil.sign(bytes, privateKey);
        System.out.println("密文：" + encryptDate + "\n");
        System.out.println("加签：" + sign);
        return sign + "&" + encryptDate;
    }

    /**
     * 手动解签解密
     */
    @PostMapping("/verify")
    public String verify(@RequestBody String str) {
        String[] data = str.split("&");
        boolean verify = CreateRsaSecrteKeyUtil.verify(data[1].getBytes(), data[0], publicKey);
        if (verify) {
            Consumer consumer = JSON.parseObject(CreateRsaSecrteKeyUtil.decryptRSADate(privateKey, data[1]), Consumer.class);
            return "验签成功：" + consumer.toString();
        } else {
            return "验签失败！";
        }
    }

}
