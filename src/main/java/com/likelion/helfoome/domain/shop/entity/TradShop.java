package com.likelion.helfoome.domain.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tradShop")
public class TradShop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tradShopName", nullable = false)
  private String shopName;

  @Column(name = "tradDescription", nullable = false)
  private String description;

  @Column(name = "tradShopAddr", nullable = false)
  private String tradShopAddr;

  @Column(name = "tradShopImageUrl", nullable = false)
  private String tradShopImageUrl;
}
