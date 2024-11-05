package com.likelion.helfoome.domain.shop.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

  private Long shopId;
  private String productName;
  private String description;
  private String price;
  private String discountPrice;
  private String discountPercent;
  private Integer quantity;
  private Boolean isSelling;
  private List<String> imageUrls;
}
