package me.archie.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.archie.springbootdeveloper.domain.Comment;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCommentResponse {
  private Long id;
  private String content;

  public AddCommentResponse(Comment comment){
    this.id = comment.getId();
    this.content = comment.getContent();
  }
}
