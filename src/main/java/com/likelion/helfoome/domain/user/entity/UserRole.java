package com.likelion.helfoome.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  USER("ROLE_USER", "일반 회원"),
  SELLER("ROLE_SELLER", "판매 회원"),
  ADMIN("ROLE_ADMIN", "관리자");

  private final String key;
  private final String title;
}
