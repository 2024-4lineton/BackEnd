package com.likelion.helfoome.domain.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopRegisterRequest {
  private String shopName;
  private Integer shopType;
  private Integer category;
  private String taxId;
  private String businessHours;
  private String dayOff;
  private String shopAddr;
  private String shopContact;
  private String shopImageName;
  private String shpImageURL;
}
