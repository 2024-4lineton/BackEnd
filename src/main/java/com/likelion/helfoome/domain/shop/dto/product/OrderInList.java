package com.likelion.helfoome.domain.shop.dto.product;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInList {

  private Long orderId;
  private String nickname;
  private String PIN;
  private LocalDateTime orderTime;
}
