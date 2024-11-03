package com.likelion.helfoome.global.auth.handler;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.global.auth.dto.CustomOAuth2User;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  private static final String AUTHORIZATION_COOKIE = "Authorization";
  private static final String REFRESH_TOKEN_COOKIE = "RefreshToken";
  private static final String DEFAULT_ROLE = "ROLE_USER";
  private static final String REDIRECT_URL = "https://metalog.store/swagger-ui/index.html";
  private static final int COOKIE_MAX_AGE = 60 * 60; // 1시간

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
    String email = customUserDetails.getEmail();

    // 역할 가져오기
    String role =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse(DEFAULT_ROLE);

    // 토큰 생성
    String accessToken = jwtUtil.createAccessToken(email, role);
    String refreshToken = jwtUtil.createRefreshToken(email);

    // Refresh 토큰 저장
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    user.setRefreshToken(refreshToken);
    userRepository.save(user);

    // 쿠키 설정 및 추가
    response.addCookie(createCookie(AUTHORIZATION_COOKIE, accessToken));
    response.addCookie(createCookie(REFRESH_TOKEN_COOKIE, refreshToken));

    // 인증 성공 후 리다이렉트
    response.sendRedirect(REDIRECT_URL);
  }

  private Cookie createCookie(String name, String value) {
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(COOKIE_MAX_AGE);
    cookie.setHttpOnly(true);
    cookie.setPath("/"); // 쿠키 적용 경로 설정
    cookie.setSecure(true); // HTTPS 환경에서만 사용할 경우 설정

    return cookie;
  }
}
