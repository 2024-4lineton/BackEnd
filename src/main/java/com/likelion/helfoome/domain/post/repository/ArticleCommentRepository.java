package com.likelion.helfoome.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.ArticleComment;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
  List<ArticleComment> findByArticleId(Long articleId);

  List<ArticleComment> findByUser_Email(String email);
}
