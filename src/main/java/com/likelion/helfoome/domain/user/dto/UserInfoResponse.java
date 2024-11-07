package com.likelion.helfoome.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
  private String profileImgUrl;
  private String phone;
  private String birth;
  private String gender;
  private String activityLocation;
  private String foodCategory;
}
