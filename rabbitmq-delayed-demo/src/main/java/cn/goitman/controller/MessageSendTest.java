package cn.goitman.controller;

import cn.goitman.component.ProducerSender;
import cn.goitman.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nicky
 * @version 1.0
 * @className MessageSendTest
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 测试
 * @date 2021/7/9 17:06
 */
@RestController
public class MessageSendTest {

    @Autowired
    private ProducerSender producerSender;

    @PostMapping("/delayedSend")
    public void delayedSend(@RequestBody Order order){
        producerSender.sendMessage(order);
    }
}
