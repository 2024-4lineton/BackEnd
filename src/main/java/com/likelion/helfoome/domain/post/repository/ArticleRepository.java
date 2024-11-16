package com.likelion.helfoome.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  List<Article> findByUser_Email(String email);

  List<Article> findByContentContaining(String keyword);
}
