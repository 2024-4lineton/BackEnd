package com.likelion.helfoome.domain.shop.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.likelion.helfoome.domain.order.entity.Order;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shop")
public class Shop extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "shopName", nullable = false)
  private String shopName;

  @Column(name = "shopType", nullable = false)
  private Integer shopType;

  @Column(name = "marketName")
  private String marketName;

  @Column(name = "taxId", nullable = false, unique = true)
  private String taxId;

  @Column(name = "businessHours", nullable = false)
  private String businessHours;

  @Column(name = "dayOff")
  private String dayOff;

  @Column(name = "shopAddr", nullable = false, unique = true)
  private String shopAddr;

  @Column(name = "shopContact", nullable = false, unique = true)
  private String shopContact;

  @Column(name = "shopImageName", nullable = false)
  private String shopImageName;

  @Column(name = "shopImageURL", nullable = false)
  private String shopImageURL;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  @JsonIgnore
  private User user;

  @OneToMany(
      mappedBy = "shop",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Product> productList;

  @OneToMany(
      mappedBy = "shop",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Order> orderList;
}
