package cn.goitman.pojo;

/**
 * @author Nicky
 * @version 1.0
 * @className Like
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 文章点赞表
 * @date 2022/5/12 20:12
 */
public class Likes {

    // 文章id
    private String articleId;

    // 点赞用户id
    private String userId;

    // 点赞状态
    private Integer status;

    public Likes() {
    }

    public Likes(String articleId, String userId, Integer status) {
        this.articleId = articleId;
        this.userId = userId;
        this.status = status;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
