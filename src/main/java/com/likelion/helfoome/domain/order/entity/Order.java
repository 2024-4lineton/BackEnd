package com.likelion.helfoome.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "productId", nullable = false)
  private Long productId;

  @Column(name = "productName", nullable = false)
  private String productName;

  @Column(name = "mainImage", nullable = false)
  private String mainImage;

  @Column(name = "PIN", nullable = false)
  private String PIN;

  @Column(name = "orderStatus", nullable = false)
  private Integer orderStatus;

  @Column(name = "totalPrice", nullable = false)
  private String totalPrice;

  @Column(name = "totalQuantity", nullable = false)
  private Integer totalQuantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shopId", referencedColumnName = "id", nullable = false)
  private Shop shop;
}
