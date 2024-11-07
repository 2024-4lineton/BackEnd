package com.likelion.helfoome.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

  private Long shopId;
  private Long productId;
  private String userEmail;
  private Integer quantity;
}
