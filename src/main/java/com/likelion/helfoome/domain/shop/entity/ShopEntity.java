package com.likelion.helfoome.domain.shop.entity;

import jakarta.persistence.*;

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
public class ShopEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;

  @Column(name = "shopName", nullable = false)
  private String shopName;

  @Column(name = "shopType", nullable = false)
  private Integer shopType;

  @Column(name = "category", nullable = false)
  private Integer category;

  @Column(name = "taxId", nullable = false, unique = true)
  private String taxId;

  @Column(name = "businessHours")
  private String businessHours;

  @Column(name = "dayOff")
  private String dayOff;

  @Column(name = "shopAddr", nullable = false, unique = true)
  private String shopAddr;

  @Column(name = "shopContact", nullable = false, unique = true)
  private String shopContact;

  @Column(name = "shopImageName")
  private String shopImageName;

  @Column(name = "shopImageURL")
  private String shopImageURL;

  @Column(name = "marketName", nullable = false)
  private String marketName;
}
