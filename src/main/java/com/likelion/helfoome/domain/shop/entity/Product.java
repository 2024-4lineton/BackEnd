package com.likelion.helfoome.domain.shop.entity;

import com.likelion.helfoome.domain.shop.dto.product.ProductResponse;
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
@Table(name = "product")
public class Product extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "productName", nullable = false)
  private String productName;

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


  public void updateQuantity(int newQuantity) {
    if (newQuantity < 0) {
      throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
    }
    if (newQuantity == 0) {
      this.isSelling = false;
    }
    this.quantity = newQuantity;
  }

  public ProductResponse toProductResponse() {
    ProductResponse response = new ProductResponse();
    response.setShopId(this.shop.getId());
    response.setShopName(this.shop.getShopName()); // Shop 엔티티에 이름 필드가 있다고 가정
    response.setProductId(this.id);
    response.setProductName(this.productName);
    response.setPrice(this.price);
    response.setDiscountPrice(this.discountPrice);
    response.setDiscountPercent(this.discountPercent);
    response.setQuantity(this.quantity);
    response.setIsSelling(this.isSelling);
    response.setProductImgUrl(this.productImageURL);
    return response;
  }
}
