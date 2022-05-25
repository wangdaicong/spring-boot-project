package cn.goitman.mapper;

import cn.goitman.pojo.Article;
import cn.goitman.pojo.Likes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className LikeDao
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description
 * @date 2022/5/19 17:13
 */
@Mapper
public interface LikeDao {

    Likes getLikesList(Likes likes);

    void saveLike(Likes likes);

    void updataLike(Likes likes);

    Article getArticleData(String articleId);

    void saveArticleList(List<Article> list);

    void saveArticle(Article article);

    void updataArticle(Article article);
}
