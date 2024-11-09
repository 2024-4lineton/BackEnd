package com.likelion.helfoome.domain.user.entity;

import com.likelion.helfoome.domain.cart.entity.Cart;
import com.likelion.helfoome.domain.cart.entity.CartProduct;
import com.likelion.helfoome.domain.shop.entity.Shop;
import jakarta.persistence.*;

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
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "cartProduct_id")
  private CartProduct cartProduct;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "shop_id")
  private Shop shop;


}
