package cn.goitman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Nicky
 * @version 1.0
 * @className RedisConfig
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Redis配置类
 * @date 2022/5/16 14:40
 */
@Configuration
public class RedisConfig {

    /**
    * 凡事使用到template的redis操作都必须走@Transanctional注解式事务，要不然会导致连接一直占用，不关闭
    */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate template = new RedisTemplate();
        // 改变redisTemplate的序列化方式，key为字符串格式，value为json格式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // HashKey 和 HashValue 为json格式
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        // 开启事务支持
        template.setEnableTransactionSupport(true);
        return template;
    }

    /**
    * 配置事务管理器
    */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }
}
