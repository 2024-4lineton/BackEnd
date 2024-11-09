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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.likelion.helfoome.domain.cart.entity.CartProduct;
import com.likelion.helfoome.global.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product")
public class Product extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "productName", nullable = false)
  private String productName;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "price", nullable = false)
  private String price;

  @Column(name = "discountPrice", nullable = false)
  private String discountPrice;

  @Column(name = "discountPercent", nullable = false)
  private String discountPercent;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "isSelling", nullable = false)
  private Boolean isSelling;

  @Column(name = "realAddr", nullable = false)
  private String realAddr;

  @Column(name = "productImageName", nullable = false)
  private String productImageName;

  @Column(name = "productImageURL", nullable = false)
  private String productImageURL;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shopId", referencedColumnName = "id", nullable = false)
  private Shop shop;

  @OneToMany(
      mappedBy = "product",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<CartProduct> cartProductList;

  public void updateQuantity(int newQuantity) {
    if (newQuantity < 0) {
      throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
    }
    if (newQuantity == 0) {
      this.isSelling = false;
    }
    this.quantity = newQuantity;
  }
}
