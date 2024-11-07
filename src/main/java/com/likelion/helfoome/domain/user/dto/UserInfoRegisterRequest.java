package com.likelion.helfoome.domain.user.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRegisterRequest {
  private MultipartFile profileImgName;
  private MultipartFile profileImgUrl;
  private String phone;
  private String birth;
  private String gender;
  private String activityLocation;
  private String foodCategory;
  private Boolean ToS;
  private Boolean privacyPolicy;
  private Boolean LBS;
  private Boolean marketingPolicy;
}
