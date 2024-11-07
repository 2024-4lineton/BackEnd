package com.likelion.helfoome.domain.user.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.user.dto.UserInfoRegisterRequest;
import com.likelion.helfoome.domain.user.dto.UserInfoResponse;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.entity.UserInfo;
import com.likelion.helfoome.domain.user.repository.UserInfoRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.global.S3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoService {
  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final S3Service s3Service;

  // 첫 로그인 확인
  public Boolean isFirstLogin(String email) {
    userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    return userInfoRepository.findByUser_Email(email).isEmpty();
  }

  // 쿠키로 현재 사용자 email 받아와서 사용자 정보 생성
  public String userInfoRegister(String email, UserInfoRegisterRequest request) throws IOException {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

    UserInfo newUserInfo = new UserInfo();
    newUserInfo.setUser(user);
    newUserInfo.setPhone(request.getPhone());
    newUserInfo.setBirth(request.getBirth());
    newUserInfo.setGender(request.getGender());
    newUserInfo.setActivityLocation(request.getActivityLocation());
    newUserInfo.setFoodCategory(request.getFoodCategory());
    newUserInfo.setToS(request.getToS());
    newUserInfo.setPrivacyPolicy(request.getPrivacyPolicy());
    newUserInfo.setLBS(request.getLBS());
    newUserInfo.setMarketingPolicy(request.getMarketingPolicy());
    newUserInfo.setProfileImageName(request.getProfileImgName().getOriginalFilename());
    String imgUrl = s3Service.upload(request.getProfileImgUrl(), "shopImages");
    newUserInfo.setProfileImageURL(imgUrl);

    userInfoRepository.save(newUserInfo);

    return "개인정보가 정상적으로 등록되었습니다.";
  }

  // 특정 사용자 정보 단일 조회
  public UserInfoResponse getUserInfoByEmail(String email) {
    log.info("Get userinfo for email: {}", email);

    UserInfoResponse response = new UserInfoResponse();

    // Email에 해당하는 사용자 정보 단일 조회
    Optional<UserInfo> userInfo = userInfoRepository.findByUser_Email(email);
    if (userInfo.isPresent()) {
      response =
          new UserInfoResponse(
              userInfo.get().getProfileImageURL(),
              userInfo.get().getPhone(),
              userInfo.get().getBirth(),
              userInfo.get().getGender(),
              userInfo.get().getActivityLocation(),
              userInfo.get().getFoodCategory());

      log.warn("No user info found: {}", email);
    }
    return response;
  }
}
