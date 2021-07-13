package cn.goitman.component;

import cn.goitman.pojo.Order;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Nicky
 * @version 1.0
 * @className ConsumerReceiver
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 消息消费者
 * @date 2021/7/9 16:07
 */
@Component
public class ConsumerReceiver {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerReceiver.class);

    /**
     * ·@RabbitListener可以标注在类上，当在类上时需@RabbitHandler配合使用，
     * 如有多个@RabbitHandler，根据MessageConverter转换后的对象来匹配哪个方法处理
     *
     * ·@RabbitListener(queues = "delayedQueue",containerFactory = "")
     * ·containerFactory：可指定一个RabbitListenerContainerFactory的bean，默认为rabbitListenerContainerFactory的实例
     *  也可在rabbitListenerContainerFactory实例上的@Bean注解中标记名称如：@Bean("rabbitListenerContainerFactory2")
     */
    @RabbitListener(queues = "delayedQueue")
    public void receiverMessage(Message msg , Channel channel) throws IOException {
        // 应避免脏数据的接收，若数据一直消费失败而退回队列，队列又一直发送数据给消费者，将造成无限循环，导致内存溢出系统崩溃
        Order order = JSONObject.parseObject(new String(msg.getBody(),"UTF-8"), Order.class);
        logger.info("order:{}",order.toString());

        // 获取消息数量，可和批量确认一起使用
        // channel.basicQos(10);

        // boolean flag = ****(); 在此做逻辑，返回boolean类型决定消息是走确认机制，还是退回机制
        boolean flag = true;

        if (flag) {
            /*
            * 确认机制，参数一：消息唯一标识；参数二：是否批量确认，false为不开启
            * 若开启批量确认，最后一条确认的ID，会把之前未确认的消息一并确认
            * 开启批量后需做好幂等性处理，若消息在未确认之前，连接中断会造成重复消费
            */
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
            logger.info("消费成功，ID:{}",msg.getMessageProperties().getDeliveryTag());
        }else {
            /*
            * 退回机制，参数一：唯一标识符；参数二：是否批量退回，false为单条退回；参数三：是否把消息退回队列中，false为废弃消息
            * 若有多个消费者需做好幂等性处理，避免消息重复消费
            */
            channel.basicNack(msg.getMessageProperties().getDeliveryTag(),false,true);
            logger.info("消费失败，ID:{}",msg.getMessageProperties().getDeliveryTag());
        }
    }
}
