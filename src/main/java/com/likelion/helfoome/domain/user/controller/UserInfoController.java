package com.likelion.helfoome.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.user.dto.UserInfoRegisterRequest;
import com.likelion.helfoome.domain.user.dto.UserInfoResponse;
import com.likelion.helfoome.domain.user.service.StampService;
import com.likelion.helfoome.domain.user.service.UserInfoService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/userInfo")
public class UserInfoController {

  private final UserInfoService userInfoService;
  private final StampService stampService;
  private final JwtUtil jwtUtil;

  // 사용자 첫 로그인 확인
  @Operation(summary = "사용자 첫 로그인 확인", description = "사용자 첫 로그인 확인")
  @PostMapping("/check-first-login")
  public Boolean userInfoRegister(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      // 서비스 메서드 호출
      return userInfoService.isFirstLogin(email);
    } catch (Exception e) {
      log.error("Error occurred while checking first login: {}", e.getMessage());
      return null;
    }
  }

  // 사용자 정보 등록하기
  @Operation(summary = "사용자 정보 등록", description = "사용자 첫 로그인 후 개인정보 등록")
  @PostMapping("/register")
  public ResponseEntity<?> userInfoRegister(
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody UserInfoRegisterRequest request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      // 서비스 메서드 호출
      String result = userInfoService.userInfoRegister(email, request);

      if ("개인정보가 정상적으로 등록되었습니다.".equals(result)) {
        stampService.createStamp(email); // 정보 정상 등록시 Stamp생성
        return ResponseEntity.ok(result);
      } else {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
      }
    } catch (Exception e) {
      log.error("Error occurred while registering user info: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error occurred registering user info: " + e.getMessage());
    }
  }

  @Operation(summary = "사용자 개인정보 조회", description = "로그인된 사용자 개인정보 조회")
  @GetMapping("/my-info")
  public ResponseEntity<?> getUserInfoByEmail(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      UserInfoResponse response = userInfoService.getUserInfoByEmail(email);
      if (response == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("사용자 개인정보가 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(response);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching userInfo in controller /api/userInfo: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
