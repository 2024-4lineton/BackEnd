package com.likelion.helfoome.domain.shop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainProductResponse {
  private String shop;
  private Long productId;
  private String productName;
  private String discountPrice;
  private String discountPercent;
  private String imgUrl;
}
