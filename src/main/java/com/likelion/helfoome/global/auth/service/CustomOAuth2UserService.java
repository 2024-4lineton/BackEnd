package com.likelion.helfoome.global.auth.service;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.user.dto.UserResponse;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.global.auth.dto.CustomOAuth2User;
import com.likelion.helfoome.global.auth.dto.GoogleResponse;
import com.likelion.helfoome.global.auth.dto.KakaoResponse;
import com.likelion.helfoome.global.auth.dto.NaverResponse;
import com.likelion.helfoome.global.auth.dto.OAuth2Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Response oAuth2Response;

    // 소셜 로그인 provider별 응답 처리
    switch (registrationId) {
      case "naver" -> oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
      case "google" -> oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
      case "kakao" -> oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
      default -> throw new OAuth2AuthenticationException(
          "Unsupported registrationId: " + registrationId);
    }

    String email = oAuth2Response.getProvider() + " " + oAuth2Response.getEmail();
    Optional<User> existData = userRepository.findByEmail(email);

    // 존재하는 사용자 데이터 설정
    UserResponse userResponse = new UserResponse();
    userResponse.setEmail(email);
    userResponse.setNickname(oAuth2Response.getNickname());

    if (existData.isEmpty()) {
      // 새 사용자 생성 및 저장
      User newUser = new User();
      newUser.setEmail(email);
      newUser.setNickname(oAuth2Response.getNickname());
      newUser.setUserRole("ROLE_USER");

      userRepository.save(newUser);
      userResponse.setRole("ROLE_USER");

    } else {
      // 기존 사용자 업데이트 및 저장
      User existingUser = existData.get();
      existingUser.setNickname(oAuth2Response.getNickname());
      userRepository.save(existingUser);

      userResponse.setRole(existingUser.getUserRole());
    }

    return new CustomOAuth2User(userResponse);
  }
}
