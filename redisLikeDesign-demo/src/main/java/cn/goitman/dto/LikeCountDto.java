package cn.goitman.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Nicky
 * @version 1.0
 * @className LikeDto
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 数据传输封装类
 * @date 2022/5/13 15:07
 */
public class LikeCountDto implements Serializable {

    @NotBlank
    private String articleId;

    @NotBlank
    private String userId;

    public LikeCountDto() {
    }

    public LikeCountDto(String articleId, String userId) {
        this.articleId = articleId;
        this.userId = userId;
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
}
