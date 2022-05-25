package cn.goitman.pojo;

/**
 * @author Nicky
 * @version 1.0
 * @className Article
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 文章点赞数量表
 * @date 2022/5/13 16:32
 */
public class Article {

    // 文章id
    private String articleId;

    // 文章点赞数
    private Integer LikeCount;

    public Article() {
    }

    public Article(String articleId, Integer likeCount) {
        this.articleId = articleId;
        LikeCount = likeCount;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(Integer likeCount) {
        LikeCount = likeCount;
    }
}
