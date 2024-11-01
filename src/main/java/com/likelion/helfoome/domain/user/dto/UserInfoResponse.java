package com.likelion.helfoome.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
  private String nickname;
  private String email;
  private String role;
}
