package cn.goitman.service;

import cn.goitman.pojo.Article;
import cn.goitman.pojo.Likes;

import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className RedisService
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Redis接口
 * @date 2022/5/13 16:05
 */
public interface RedisService {

    void saveLike(String articleId, String userId);

    void unLike(String articleId, String userId);

    void deleteLike(List<Likes> list);

    void deleteLikeCount(String articleId);

    List<Likes> getAllLikeData();

    List<Article> getArticleLikeCount();

    List<String> fuzzyQueryHashKey(String articleId);

    List<String> fuzzyQueryKey(String key);
}
