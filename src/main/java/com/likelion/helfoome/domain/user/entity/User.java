package com.likelion.helfoome.domain.user.entity;

import java.util.List;

import jakarta.persistence.*;

import com.likelion.helfoome.domain.cart.entity.Cart;
import com.likelion.helfoome.domain.cart.entity.CartProduct;
import com.likelion.helfoome.domain.post.entity.Article;
import com.likelion.helfoome.domain.post.entity.ArticleComment;
import com.likelion.helfoome.domain.post.entity.ArticleLike;
import com.likelion.helfoome.domain.post.entity.Community;
import com.likelion.helfoome.domain.post.entity.CommunityComment;
import com.likelion.helfoome.domain.post.entity.CommunityLike;
import com.likelion.helfoome.domain.post.entity.Demand;
import com.likelion.helfoome.domain.post.entity.DemandLike;
import com.likelion.helfoome.domain.post.entity.Supply;
import com.likelion.helfoome.domain.post.entity.SupplyLike;
import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "nickname", nullable = false, unique = true)
  private String nickname;

  @Column(name = "refresh_token", unique = true)
  private String refreshToken;

  @Column(name = "role", nullable = false)
  private String userRole = "ROLE_USER";

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "userInfo_id")
  private UserInfo userInfo;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "stamp_id")
  private Stamp stamp;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "shop_id")
  private Shop shop;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "cartProduct_id")
  private CartProduct cartProduct;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Notification> notificationList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Article> articleList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<ArticleLike> articleLikeList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<ArticleComment> articleCommentList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Community> communityList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<CommunityLike> communityLikeList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<CommunityComment> communityCommentList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Demand> demandList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<DemandLike> demandLikeList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Supply> supplyList;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<SupplyLike> supplyLikeList;
}
