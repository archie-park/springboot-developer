package me.archie.springbootdeveloper.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.archie.springbootdeveloper.domain.Article;
import me.archie.springbootdeveloper.domain.Comment;
import me.archie.springbootdeveloper.dto.AddArticleRequest;
import me.archie.springbootdeveloper.dto.AddCommentRequest;
import me.archie.springbootdeveloper.dto.UpdateArticleRequest;
import me.archie.springbootdeveloper.repository.BlogRepository;
import me.archie.springbootdeveloper.repository.CommentRepository;

@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {

  private final BlogRepository blogRepository;
  private final CommentRepository commentRepository;

  // 블로그 글 추가 메서드
  public Article save(AddArticleRequest request, String userName) {
    return blogRepository.save(request.toEntity(userName));
  }

  public List<Article> findAll() {
    return blogRepository.findAll();
  }

  public Article findById(long id) {
    return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
  }

  public void delete(long id) {
    Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));

    authorizeArticleAuthor(article);
    blogRepository.delete(article);
  }

  @Transactional // 트랜잭션 메서드
  public Article update(long id, UpdateArticleRequest request) {
    Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));

    authorizeArticleAuthor(article);
    article.update(request.getTitle(), request.getContent());

    return article;
  }

  // 게시글을 작성한 유저인지 확인
  private static void authorizeArticleAuthor(Article article) {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    if (!article.getAuthor().equals(userName)) {
      throw new IllegalArgumentException("not authorized");
    }
  }

  public Comment addComment(AddCommentRequest request, String userName) {
    Article article = blogRepository.findById(request.getArticleId())
        .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getArticleId()));
    return commentRepository.save(request.toEntity(userName, article));
  }
}
