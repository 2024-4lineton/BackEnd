package com.likelion.helfoome.domain.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradShopInList {

  private Long shopId;
  private String shopName;
  private Long distance;
  private Integer productCount;
  private String imgUrl;
  private String description;
}
