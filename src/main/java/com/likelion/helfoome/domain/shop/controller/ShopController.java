package com.likelion.helfoome.domain.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.shop.dto.ShopList;
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

  @Operation(
      summary = "사업자 번호 유효성 검사",
      description = "전송된 사업자 번호 또는 사용자의 가게가 DB에 존재하는지 확인. 이미 존재할 경우 True, 존재하지 않을 경우 False")
  @PostMapping("/tax-id-exist")
  public Boolean isTaxIdExist(
      @RequestHeader("Authorization") String bearerToken, @RequestBody TaxIdRequest request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      return shopService.isTaxIdExist(email, request.getTaxId());
    } catch (Exception e) {
      log.error("Error during find Tax ID: {}", e.getMessage());
      return null;
    }
  }

  // 가게 등록하기
  @Operation(
      summary = "가게 등록",
      description = "사업자 인증 후 가게 등록(shopType : 0(전통시장) || 1(골목시장) || 2(브랜드)")
  @PostMapping("/register")
  public ResponseEntity<?> shopRegister(
      @RequestHeader("Authorization") String bearerToken,
      @ModelAttribute ShopRegisterRequest request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      // 서비스 메서드 호출
      String result = shopService.storeRegister(email, request);

      if ("가게가 성공적으로 등록되었습니다.".equals(result)) {
        return ResponseEntity.ok(result);
      } else {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("가게 등록에 실패하였습니다.");
      }
    } catch (Exception e) {
      log.error("Error occurred while registering the store: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error occurred registering the store: " + e.getMessage());
    }
  }

  // 전통시장 정렬 리스트
  @Operation(summary = "전통시장 리스트 가져오기", description = "sort는 0(거리순) / 1(상품 많은 순)")
  @GetMapping("/traditional/sorted")
  public ResponseEntity<ShopList> getSortedShops(
      @RequestHeader("Authorization") String bearerToken, @RequestParam int sort) {
    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String email = claims.getId();
    ShopList response = shopService.getSortedShopList(email, sort);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
