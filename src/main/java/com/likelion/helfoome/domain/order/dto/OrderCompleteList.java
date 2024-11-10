package com.likelion.helfoome.domain.order.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCompleteList {

  private String shopName;
  private String productName;
  private LocalDateTime createdAt;
  private Integer orderState;

}
