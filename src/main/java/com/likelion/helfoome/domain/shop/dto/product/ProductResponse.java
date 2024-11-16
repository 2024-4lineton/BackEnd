package com.likelion.helfoome.domain.shop.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

  private Long shopId;
  private String shopName;
  private Long productId;
  private String productName;
  private String price;
  private String discountPrice;
  private String discountPercent;
  private Integer quantity;
  private Boolean isSelling;
  private String realAddr;
  private String productImgUrl;
}
