package me.archie.springbootdeveloper.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.archie.springbootdeveloper.domain.Article;
import me.archie.springbootdeveloper.domain.Comment;
import me.archie.springbootdeveloper.dto.AddArticleRequest;
import me.archie.springbootdeveloper.dto.AddCommentRequest;
import me.archie.springbootdeveloper.dto.AddCommentResponse;
import me.archie.springbootdeveloper.dto.ArticleResponse;
import me.archie.springbootdeveloper.dto.UpdateArticleRequest;
import me.archie.springbootdeveloper.service.BlogService;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

  private final BlogService blogService;

  // HTTP 메서드가 POST일때 전달받은 URL과 동일하면 메서드로 매핑
  @PostMapping("/api/articles")
  public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal) {
    Article savedArticle = blogService.save(request, principal.getName());

    // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
    return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
  }

  @GetMapping("/api/articles")
  public ResponseEntity<List<ArticleResponse>> findAllArticles() {
    List<ArticleResponse> articles = blogService.findAll().stream().map(ArticleResponse::new).toList();

    return ResponseEntity.ok().body(articles);
  }

  @GetMapping("/api/articles/{id}")
  // URL 경로에서 값 추출
  public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
    Article article = blogService.findById(id);

    return ResponseEntity.ok().body(new ArticleResponse(article));
  }

  @DeleteMapping("/api/articles/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
    blogService.delete(id);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/api/articles/{id}")
  public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
    Article updatedArticle = blogService.update(id, request);

    return ResponseEntity.ok().body(updatedArticle);
  }

  @PostMapping("/api/comments")
  public ResponseEntity<AddCommentResponse> addComment(@RequestBody AddCommentRequest request, Principal principal){
    Comment savedComment = blogService.addComment(request, principal.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(new AddCommentResponse(savedComment));
  }
}
