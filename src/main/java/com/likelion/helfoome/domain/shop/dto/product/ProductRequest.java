package com.likelion.helfoome.domain.shop.dto.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

  private Long shopId;
  private String productName;
  private String description;
  private String price;
  private String discountPrice;
  private String discountPercent;
  private Integer quantity;
  private Boolean isSelling;
  private String realAddr;
  private List<MultipartFile> images;
}