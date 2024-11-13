package com.likelion.helfoome.domain.shop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LastProduct {

  private String shopName;
  private Long productId;
  private String productName;
  private String discountPrice;
  private String discountPercent;
  private String imgUrl;
  private String endTime;
}
