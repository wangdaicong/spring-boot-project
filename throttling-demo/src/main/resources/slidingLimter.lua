---
--- Created by Nicky.
--- blog：goitman.cn | blog.csdn.net/minkeyto
--- DateTime: 2022/3/17 17:20
--- 滑动窗口限流
---

--- 移除时间窗口之前的数据
redis.call('zremrangeByScore', KEYS[1], 0, ARGV[1])
--- 统计当前元素数量
local res = redis.call('zcard', KEYS[1])
--- 是否超过阈值，判断key是否存在，不存在则创建一个空的有序集并执行
if (res == nil) or (res < tonumber(ARGV[3])) then
    redis.call('zadd', KEYS[1], ARGV[2], ARGV[4])
    return 1
else
    return 0
end