package com.likelion.helfoome.domain.post.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.likelion.helfoome.domain.Img.entity.ArticleImg;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "article")
public class Article extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "totalLikes", nullable = false)
  private Integer totalLikes;

  @Column(name = "totalComments", nullable = false)
  private Integer totalComments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;

  @OneToMany(
      mappedBy = "article",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<ArticleComment> articleCommentList;

  @OneToMany(
      mappedBy = "article",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<ArticleLike> articleLikeList;

  @OneToMany(
      mappedBy = "article",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<ArticleImg> articleImgList;
}
