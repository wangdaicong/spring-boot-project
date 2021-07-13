package cn.goitman.component;

import cn.goitman.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Nicky
 * @version 1.0
 * @className ProducerSender
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 消息生产者
 * @date 2021/7/9 16:06
 */
@Component
public class ProducerSender {

    private final static Logger logger = LoggerFactory.getLogger(ProducerSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

/**
     * 消息发送确认回调方法，确保消息是否发送到交换机
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {

        /**
         * correlationData：SpringBoot提供的业务标识对象，封装业务ID信息，需要在发送消息时传入此参数，这里才能接收到，否则是null
         * ack：消息发送的结果状态，成功是true，失败是false
         * cause：发送失败的描述信息，如果发送成功是null。
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            logger.info("correlationData:{},ack:{},cause:{}",correlationData.toString(), ack, cause);
        }
    };

    /**
     * 消息发送失败回调方法，可能是队列或路由键不存在等等
     */
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        /**
         * message：发送的信息内容
         * replyCode：状态码，200为成功
         * replyText：失败信息
         * exchange：交换机名称
         * routingKey：路由键
         */
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            logger.info("returnedMessage:{},replyCode:{},replyText:{},exchange:{},routingKey:{}",
                    new String(message.getBody()), replyCode, replyText, exchange, routingKey);
        }
    };

    /**
    * 消息发送，一般定时任务配合
    */
    public void sendMessage(Object message) {
        // 消息发送确认，处理消息到交换机之间的逻辑
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 设为true，消息不会自动删除，而被return消息模式监听
        rabbitTemplate.setMandatory(true);
        // 消息失败监听，处理交换机到队列之间的逻辑
        rabbitTemplate.setReturnCallback(returnCallback);
        // 生产者JSON数据序列化
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        // 消息发送，标准发送信息和延时发送差异在于MessagePostProcessor
        // rabbitTemplate.convertAndSend(RabbitmqConfig.DELAYED_EXCHANGE,"delayed.boot",message,new CorrelationData(UUID.randomUUID().toString().replace("-","")));
        rabbitTemplate.convertAndSend(RabbitmqConfig.DELAYED_EXCHANGE, "delayed.boot", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息持久化
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                // 设置消息延迟发送时间，单位毫秒ms
                message.getMessageProperties().setDelay(6000);
                return message;
            }
            // 消息唯一ID
        }, new CorrelationData(UUID.randomUUID().toString().replace("-", "")));
    }
}
