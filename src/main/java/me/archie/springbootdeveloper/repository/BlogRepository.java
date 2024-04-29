package me.archie.springbootdeveloper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.archie.springbootdeveloper.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long>{
  
}
