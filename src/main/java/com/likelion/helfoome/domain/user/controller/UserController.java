package com.likelion.helfoome.domain.user.controller;

import java.util.List;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.user.dto.UserResponse;
import com.likelion.helfoome.domain.user.service.UserService;
import com.likelion.helfoome.global.auth.dto.AuthResponse;
import com.likelion.helfoome.global.auth.dto.RefreshTokenRequest;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final JwtUtil jwtUtil;
  private final UserService userService;

  @Operation(summary = "토큰 재발급", description = "Refresh Token 검증 후, 새로운 Access Token 발급")
  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshAuthToken(@RequestBody RefreshTokenRequest request) {
    try {
      String refreshToken = request.getRefreshToken();

      // Refresh Token 유효성 검증
      if (!jwtUtil.validateToken(refreshToken, request.getEmail())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
      }

      // 새로운 Access Token 생성
      String newAccessToken = jwtUtil.createAccessToken(request.getEmail(), request.getNickname());
      return ResponseEntity.ok(new AuthResponse(newAccessToken));

    } catch (RuntimeException e) {
      // 예외 발생 시 로그 출력 및 에러 응답
      log.error(
          "Error during token refresh in controller /api/users/refresh-token: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @Operation(summary = "닉네임 중복 확인", description = "이미 존재하는 닉네임인지 확인")
  @PostMapping("/nickname")
  public Boolean checkNickname(@RequestParam String nickname) {
    try {
      return userService.isNicknameExist(nickname);
    } catch (Exception e) {
      log.error("Error occurred while checking nickname exist: {}", e.getMessage());
      return null;
    }
  }

  @Operation(summary = "액세스 토큰 반환", description = "쿠키에 있는 Access Token String으로 가져오기")
  @GetMapping("/access-token")
  public String getAccessToken(HttpServletResponse response) {
    try {
      final String authorizationHeader = response.getHeader("Authorization");
      String jwt = null;

      // Bearer 토큰인지 확인하고, JWT 토큰에서 Email 추출
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
      }
      return jwt;
    } catch (RuntimeException e) {
      log.error("Error during token in controller /api/users/access-token: {}", e.getMessage());
      return null;
    }
  }

  @Operation(summary = "로그아웃", description = "쿠키에 있는 Access Token 파기")
  @PostMapping("/log-out")
  public ResponseEntity<?> userLogOut(HttpServletResponse response) {
    try {
      // 쿠키에 있는 Access Token 제거
      Cookie cookie = new Cookie("Authorization", null);
      cookie.setMaxAge(0); // 쿠키 삭제
      cookie.setPath("/"); // 해당 경로에 있는 쿠키를 삭제
      response.addCookie(cookie);

      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      log.error("Error during log out in controller /api/users/log-out: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @Operation(summary = "사용자 전체 조회(관리자용)", description = "소셜 로그인으로 가입된 사용자 전체 조회")
  @GetMapping("/get-all")
  public ResponseEntity<?> getAllUsers() {
    try {
      List<UserResponse> responses = userService.getAllUsers();
      if (responses == null || responses.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("사용자가 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(responses);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching user list in controller /api/user: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(
      summary = "사용자 role 조회",
      description = "ROLE_USER : 일반 사용자, ROLE_SELLER : 판매자, ROLE_ADMIN : 관리자")
  @GetMapping("/getUserRole")
  public ResponseEntity<String> getUSerRole(@RequestHeader("Authorization") String bearerToken) {
    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String email = claims.getId();
    String response = userService.getUserRole(email);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
