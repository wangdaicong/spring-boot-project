package cn.goitman.service.impl;

import cn.goitman.pojo.Article;
import cn.goitman.pojo.Likes;
import cn.goitman.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Nicky
 * @version 1.0
 * @className RedisServiceImpl
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Redis接口实现类
 * @date 2022/5/13 16:43
 */
@Service
@Transactional
public class RedisServiceImpl implements RedisService {

    // 文章点赞 KEY
    public static final String KEY_ARTICLE_LIKE = "ARTICLE_LIKE";

    // 文章点赞数量 KEY
    public static final String KEY_ARTICLE_LIKE_COUNT = "ARTICLE_LIKE_COUNT";

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 保存点赞和文章点赞量
     */
    @Override
    public void saveLike(String articleId, String userId) {
        String field = getLikeKey(articleId, userId);
        redisTemplate.opsForHash().put(KEY_ARTICLE_LIKE, field, 1);
        redisTemplate.opsForHash().increment(KEY_ARTICLE_LIKE_COUNT, articleId, 1);
    }

    /**
     * 取消点赞和文章点赞量
     */
    @Override
    public void unLike(String articleId, String userId) {
        String field = getLikeKey(articleId, userId);
        redisTemplate.opsForHash().put(KEY_ARTICLE_LIKE, field, 0);
        redisTemplate.opsForHash().increment(KEY_ARTICLE_LIKE_COUNT, articleId, -1);
    }

    /**
     * 删除点赞数据
     */
    @Override
    public void deleteLike(List<Likes> list) {
        for (Likes like : list) {
            String field = getLikeKey(like.getArticleId(), like.getUserId());
            redisTemplate.opsForHash().delete(KEY_ARTICLE_LIKE, field);
        }
    }

    /**
     * 删除文章点赞量数据
     */
    @Override
    public void deleteLikeCount(String articleId) {
        redisTemplate.opsForHash().delete(KEY_ARTICLE_LIKE_COUNT, articleId);
    }

    /**
     * 获取全部点赞数据
     */
    @Override
    public List<Likes> getAllLikeData() {
        List<Likes> list = new ArrayList<>();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(KEY_ARTICLE_LIKE, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String keys = entry.getKey().toString();

            String[] keyArr = keys.split("::");
            Likes like = new Likes(keyArr[0], keyArr[1], (Integer) entry.getValue());

            list.add(like);
        }
        return list;
    }

    /**
     * 获取文章点赞量数据
     */
    @Override
    public List<Article> getArticleLikeCount() {
        List<Article> list = new ArrayList<>();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(KEY_ARTICLE_LIKE_COUNT, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String articleId = entry.getKey().toString();
            Article article = new Article(articleId, (Integer) entry.getValue());
            list.add(article);
        }
        return list;
    }

    /**
     * 模糊查询 key
     * *：通配任意多个字符
     * ?：通配单个字符
     * []：通配括号内的某一个字
     */
    @Override
    public List<String> fuzzyQueryKey(String key) {
        List<String> userIdList = (List<String>) redisTemplate.keys("*" + key + "*")
                .stream()
                .collect(Collectors.toList());

        return userIdList;
    }

    /**
     * 根据文章ID，查询点赞此文章的用户
     */
    @Override
    public List<String> fuzzyQueryHashKey(String articleId) {
        List<String> userIdList = new ArrayList<>();

        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash()
                .scan(KEY_ARTICLE_LIKE, ScanOptions.scanOptions()
                        .match("*" + articleId + "*")   // 模糊匹配
//                        .count(100) // 可选，查询条数（默认 10）
                        .build());

        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String[] keyArr = entry.getKey().toString().split("::");
            userIdList.add(keyArr[1]);
        }
        return userIdList;
    }

    /**
     * 拼接文章ID和点赞人ID作为key
     */
    private String getLikeKey(String articleId, String userId) {
        return new StringBuilder().append(articleId).append("::").append(userId).toString();
    }
}
