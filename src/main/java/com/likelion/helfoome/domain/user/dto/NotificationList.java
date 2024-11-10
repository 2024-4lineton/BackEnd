package com.likelion.helfoome.domain.user.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationList {

  private Long id;

  private String title;

  private String content;

  private String PIN;

  private LocalDateTime createdAt;
}
