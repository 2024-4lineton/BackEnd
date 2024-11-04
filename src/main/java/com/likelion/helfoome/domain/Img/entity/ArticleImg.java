package com.likelion.helfoome.domain.Img.entity;

import com.likelion.helfoome.domain.post.entity.Article;
import com.likelion.helfoome.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "articleImg")
public class ArticleImg extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "articleId", referencedColumnName = "id", nullable = false)
  private Article article;

  @Column(name = "articleImageName", nullable = false)
  private String articleImageName;

  @Column(name = "articleImageUrl", nullable = false)
  private String articleImageUrl;
}
