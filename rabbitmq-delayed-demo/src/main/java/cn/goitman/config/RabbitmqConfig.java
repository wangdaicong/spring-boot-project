package cn.goitman.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nicky
 * @version 1.0
 * @className RabbitmqConfig
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 配置类，创建交换机、路由键、队列和之间的关联绑定
 * @date 2021/7/9 17:06
 */
@Configuration
public class RabbitmqConfig {

    private final static Logger logger = LoggerFactory.getLogger(RabbitmqConfig.class);

    // 交换机名称
    public static final String DELAYED_EXCHANGE = "delayedExchange";
    // 队列名称
    public static final String DELAYED_QUEUE = "delayedQueue";
    // 路由键，#匹配一个或多个词
    public static final String DELAYED_KEY = "delayed.#";

    /**
     * 创建主题模式交换机，
     */
    @Bean
    public TopicExchange delayedExchange() {
        // 参数一：交换机名称；参数二：数据是否持久化；参数三：数据是否自动删除
        TopicExchange exchange = new TopicExchange(DELAYED_EXCHANGE, true, false);
        // 开启延迟队列
        exchange.setDelayed(true);
        return exchange;
    }

    /**
    * 创建队列
    */
    @Bean
    public Queue delayedQueue() {
        // 参数一：队列名称；参数二：数据是否持久化
        return new Queue(DELAYED_QUEUE, true);
    }

    /**
     * 绑定交换机和队列之间的联系，并配置路由键字符
     */
    @Bean
    public Binding delayedBinding(){
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(DELAYED_KEY);
    }

    /**  
    * 消费者JSON数据反序列化
    */ 
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        /* 不设置手动确认，将会报错：
         * Channel shutdown: channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - unknown delivery tag 1, class-id=60, method-id=80)
         */
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
