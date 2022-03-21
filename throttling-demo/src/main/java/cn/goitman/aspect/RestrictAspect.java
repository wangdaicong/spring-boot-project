package cn.goitman.aspect;

import cn.goitman.annotation.Restrict;
import com.google.common.base.Preconditions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className RestrictAspect
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 切面拦截
 * @date 2022/3/16 11:18
 */
@Aspect
@Component
public class RestrictAspect {

    private static Logger log = LoggerFactory.getLogger(RestrictAspect.class);

    /**
     * 因Lua脚本只接受String类型数据，使用RedisTemplate无法正确传参到Lua脚本，Lua脚本取值为空
     *
     * 先改变redisTemplate的序列化方式：redisTemplate.setKeySerializer(new StringRedisSerializer());
     * execute(RedisScript<T> script,List<K> keys,Object... args)方法中的args参数，Lua脚本取值同样为空
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> script;

    /**
     * @PostConstruct修饰的方法会在服务器加载时运行，并且只会被执行一次。
     * PostConstruct在构造函数之后执行，init（）方法之前执行。
     * 该注解的方法在整个Bean初始化中的执行顺序：
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    public void init() {
        // 初始化DefaultRedisScript，并加载Lua脚本
        script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        // 固定窗口限流
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("fixedLimter.lua")));
        // 滑动窗口限流
//        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("slidingLimter.lua")));
        log.info("Lua 脚本加载完成！");
    }

    // 注解切点
    @Pointcut("@annotation(cn.goitman.annotation.Restrict)")
    public void restrict() {
    }

    @Around("@annotation(restrict)")
    public Object around(ProceedingJoinPoint joinPoint, Restrict restrict) throws Throwable {
        // 获取代理类，并判断是否属于MethodSignature
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("@Restrict 注解只能在方法上使用！");
        }

        // 获取注解参数
        String key = restrict.key();
        // 对key(业务标识)判空
        Preconditions.checkNotNull(key);
        // 获取限流数和过期时间，并转换为String类型
        String limitTimes = String.valueOf(restrict.limitedNumber());
        String expireTime = String.valueOf(restrict.expire());

        List<String> keyList = new ArrayList();
        keyList.add(key);
        // redis调用Lua脚本（固定窗口限流）
        Long result = (Long) stringRedisTemplate.execute(script, keyList, expireTime, limitTimes);

/*        // redis调用Lua脚本（滑动窗口限流）
        long now = System.currentTimeMillis();
        String oldest = String.valueOf(now - 1_000);
        String score = String.valueOf(now);
        // 参数三：时间戳(时间窗口)；参数四：当前时间戳作为score；参数六：当前时间戳作为score的值
        Long result = (Long) stringRedisTemplate.execute(script, keyList, oldest, score, limitTimes, score);*/

        if (result == 0) {
            log.error(restrict.msg());
            return null;
        }else {
            log.info("请求正常！");
        }
        return joinPoint.proceed();
    }





/*  // RateLimiter 单机限流
    @Around("@annotation(cn.goitman.annotation.Restrict)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Restrict restrict = method.getAnnotation(Restrict.class);

        if (restrict != null) {
            String key = restrict.key();
            RateLimiter rateLimiter = null;
            rateLimiter = StartRunner.rateLimiterMap.get(key);
            boolean acquire = rateLimiter.tryAcquire(restrict.timeout(), restrict.timeunit());
            if (!acquire) {
                log.error("{}：获取令牌失败", key);
                return null;
            } else {
                log.info("令牌桶 : {}，获取令牌成功", key);
            }
        }
        return joinPoint.proceed();
    }
*/
}
