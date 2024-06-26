package me.archie.springbootdeveloper.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@Entity // 엔티티로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

  @Id // id 필드를 기본키로 지정
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
  @Column(name = "id", updatable = false)
  private Long id;

  @Column(name = "title", nullable = false) // 'title' 이라는 not null 컬럼과 매핑
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "author", nullable = false)
  private String author;

  @CreatedDate // 엔티티가 생성될때 생성 시간 저장
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @LastModifiedDate // 엔티티가 수정될때 수정 시간 저장
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
  private List<Comment> comments;

  @Builder // 빌더 패턴으로 객체 생성
  public Article(String author, String title, String content) {
    this.author = author;
    this.title = title;
    this.content = content;
  }

  public void update(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
