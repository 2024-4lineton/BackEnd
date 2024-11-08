package com.likelion.helfoome.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {

  private String shopName;
  private String productName;
  private String discountPrice;
  private Boolean isSelling;
  private String cartProductImgUrl;
}
