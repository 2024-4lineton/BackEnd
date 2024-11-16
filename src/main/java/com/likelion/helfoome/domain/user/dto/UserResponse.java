package com.likelion.helfoome.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private String email;
  private String nickname;
  private String role;
}
