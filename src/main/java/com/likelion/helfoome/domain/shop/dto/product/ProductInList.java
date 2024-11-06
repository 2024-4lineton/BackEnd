package com.likelion.helfoome.domain.shop.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInList {

  private Long productId;

  private String shop;

  private String productName;

  private String price;

  private String discountPrice;

  private String discountPercent;

  private Integer quantity;

  private Long distance;
}
