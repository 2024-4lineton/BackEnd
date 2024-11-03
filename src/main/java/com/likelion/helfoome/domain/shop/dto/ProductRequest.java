package com.likelion.helfoome.domain.shop.dto;

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
  private List<MultipartFile> images;
}
