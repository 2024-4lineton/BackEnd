package com.likelion.helfoome.domain.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.shop.dto.ShopRegisterRequest;
import com.likelion.helfoome.domain.shop.dto.TaxIdRequest;
import com.likelion.helfoome.domain.shop.service.ShopService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class ShopController {

  private final ShopService shopService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "사업자 번호 유효성 검사", description = "전송된 사업자 번호 또는 사용자의 가게가 DB에 존재하는지 확인")
  @PostMapping("/register/taxId")
  public ResponseEntity<?> validateTaxId(
      @RequestHeader("Authorization") String bearerToken, @RequestBody TaxIdRequest request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      // 서비스 메서드 호출
      String result = shopService.checkShopExist(email, request.getTaxId());

      if ("Already existing shop. Please check your shop or Tax ID.".equals(result)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("Error during find Tax ID: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error validation in Tax Id: " + e.getMessage());
    }
  }

  // 가게 등록하기
  @Operation(summary = "가게 등록", description = "사업자 인증 후 가게 등록")
  @PostMapping("/register")
  public ResponseEntity<?> storeRegister(
      @RequestHeader("Authorization") String bearerToken,
      @ModelAttribute ShopRegisterRequest request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      // 서비스 메서드 호출
      String result = shopService.checkShopExist(email, request.getTaxId());

      if ("Already existing shop. Please check your shop or Tax ID.".equals(result)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
      } else {
        result = shopService.storeRegister(email, request);
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("Error occurred while registering the store: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error occurred registering the store: " + e.getMessage());
    }
  }
}
