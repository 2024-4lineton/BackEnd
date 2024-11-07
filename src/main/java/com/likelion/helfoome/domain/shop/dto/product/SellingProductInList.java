package com.likelion.helfoome.domain.shop.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellingProductInList {

  private Long productId;

  private String shop;

  private String productName;

  private String price;

  private Integer quantity;

  private Long orders;

  private String imgUrl;
}
