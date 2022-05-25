package cn.goitman.controller;

import cn.goitman.dto.LikeCountDto;
import cn.goitman.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Nicky
 * @version 1.0
 * @className LikeController
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 点赞控制层
 * @date 2022/5/13 15:10
 */
@RestController
public class LikeController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/like")
    public String like(@RequestBody @Validated LikeCountDto likeCountDto) {
        redisService.saveLike(likeCountDto.getArticleId(), likeCountDto.getUserId());
        return "点赞成功！";
    }

    @RequestMapping("/unlike")
    public String unlike(@RequestBody @Validated LikeCountDto likeCountDto) {
        redisService.unLike(likeCountDto.getArticleId(), likeCountDto.getUserId());
        return "取消成功！";
    }

    @RequestMapping("/fqKey/{key}")
    public List<String> fuzzyQueryKey(@PathVariable(value = "key") String key) {
        return redisService.fuzzyQueryKey(key);
    }

    @RequestMapping("/fqHashKey/{articleId}")
    public List<String> fuzzyQueryHashKey(@PathVariable(value = "articleId") String articleId) {
        return redisService.fuzzyQueryHashKey(articleId);
    }
}
