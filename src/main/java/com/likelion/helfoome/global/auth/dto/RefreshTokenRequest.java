package com.likelion.helfoome.global.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
  private String email;
  private String nickname;
  private String refreshToken;
}
