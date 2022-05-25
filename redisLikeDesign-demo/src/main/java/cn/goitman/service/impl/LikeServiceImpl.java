package cn.goitman.service.impl;

import cn.goitman.mapper.LikeDao;
import cn.goitman.pojo.Article;
import cn.goitman.pojo.Likes;
import cn.goitman.service.LikeService;
import cn.goitman.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Nicky
 * @version 1.0
 * @className LikeServiceImpl
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description Like 接口实现类
 * @date 2022/5/13 17:11
 */
@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private LikeDao likeDao;

    /**
     * 保存点赞数据到数据库
     */
    @Override
    public void savaLikeData2DB() {
        List<Likes> likeList = redisService.getAllLikeData();

        if (likeList.size() > 0) {
            for (Likes like : likeList) {
                Likes likes = likeDao.getLikesList(like);
                if (likes != null) {
                    likes.setStatus(like.getStatus());
                    likeDao.updataLike(likes);
                } else {
                    likeDao.saveLike(like);
                }
            }
            redisService.deleteLike(likeList);
        }
    }

    /**
     * 保存文章点赞量到数据库，数据基于Redis持久化
     */
/*    @Override
    public void saveArticleLikeCount2DB() {
        List<Article> articleLikeCount = redisService.getArticleLikeCount();
        if (articleLikeCount.size() > 0) {
            likeDao.saveArticleList(articleLikeCount);
        }
    }*/

    /**
     * 保存文章点赞量到数据库，Redis不持久化文章点赞量
     */
    @Override
    public void saveArticleLikeCount2DB() {
        List<Article> articleList = redisService.getArticleLikeCount();

        if (articleList.size() > 0) {
            for (Article article : articleList) {
                Article articleData = likeDao.getArticleData(article.getArticleId());

                if (articleData != null) {
                    articleData.setLikeCount(articleData.getLikeCount() + article.getLikeCount());
                    likeDao.updataArticle(articleData);
                } else {
                    likeDao.saveArticle(article);
                }
                redisService.deleteLikeCount(article.getArticleId());
            }
        }
    }
}
