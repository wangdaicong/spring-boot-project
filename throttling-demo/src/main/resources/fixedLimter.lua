---
--- Created by Nicky.
--- blog：goitman.cn | blog.csdn.net/minkeyto
--- DateTime: 2022/3/17 16:20
--- 固定窗口限流
---

--- 获取 execute(RedisScript<T> script,List<K> keys,Object... args)方法中的keys值
local key1 = KEYS[1]
--- 使用 key 做自增操作，初始值为1
local val = redis.call('incr', key1)
--- 查询key的过期时间(未设置过期时间，默认值为-1)
local ttl = redis.call('ttl', key1)
--- 获取execute(RedisScript<T> script,List<K> keys,Object... args)方法中args参数的第一个和第二个参数
local expire = ARGV[1]
local number = ARGV[2]
--- 在redis控制台打印日志
redis.log(redis.LOG_NOTICE,tostring(number))
redis.log(redis.LOG_NOTICE,tostring(expire))

redis.log(redis.LOG_NOTICE, "incr "..key1.." "..val);
--- 如果 key 值为1，设置过期时间
if val == 1 then
    redis.call('expire', key1, tonumber(expire))
else
--- 如果key已存在，并未设置过期时间的情况下，重新设置过期时间
    if ttl == -1 then
        redis.call('expire', key1, tonumber(expire))
    end
end
--- 如果自增数大于限流数，触发限流
if val > tonumber(number) then
    return 0
end
--- 未触发限流，正常请求，返回1
return 1