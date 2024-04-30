package me.archie.springbootdeveloper.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.archie.springbootdeveloper.domain.Article;
import me.archie.springbootdeveloper.domain.Comment;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {

  private Long id;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private String author;
  private List<Comment> comments;

  public ArticleViewResponse(Article article) {
    this.id = article.getId();
    this.title = article.getTitle();
    this.content = article.getContent();
    this.createdAt = article.getCreatedAt();
    this.author = article.getAuthor();
    this.comments = article.getComments();
  }
}
