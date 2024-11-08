package com.likelion.helfoome.domain.shop.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopRegisterRequest {

  private String shopName;
  private Integer shopType;
  private String marketName;
  private String taxId;
  private String businessHours;
  private String dayOff;
  private String shopAddr;
  private String shopContact;
  private MultipartFile shopImg;
}
