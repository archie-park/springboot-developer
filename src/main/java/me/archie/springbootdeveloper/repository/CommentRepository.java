package me.archie.springbootdeveloper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.archie.springbootdeveloper.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
