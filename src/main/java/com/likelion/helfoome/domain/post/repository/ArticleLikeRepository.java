package com.likelion.helfoome.domain.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.helfoome.domain.post.entity.ArticleLike;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
  Optional<ArticleLike> findByArticleIdAndUser_Email(Long articleId, String email);
}
