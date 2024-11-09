package com.likelion.helfoome.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StampResponse {

  private Boolean Sun;

  private Boolean Mon;

  private Boolean Tue;

  private Boolean Wed;

  private Boolean Thu;

  private Boolean Fri;

  private Boolean Sat;

  private Integer weekStampCount;
}
