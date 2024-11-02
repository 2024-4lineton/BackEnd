package com.likelion.helfoome.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.likelion.helfoome.global.auth.dto.AuthResponse;
import com.likelion.helfoome.global.auth.dto.RefreshTokenRequest;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final JwtUtil jwtUtil;

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
}
