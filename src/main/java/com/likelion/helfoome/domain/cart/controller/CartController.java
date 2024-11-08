package com.likelion.helfoome.domain.cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.cart.dto.AddProductRequest;
import com.likelion.helfoome.domain.cart.service.CartService;
import com.likelion.helfoome.domain.shop.dto.product.ProductResponse;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
  private final CartService cartService;
  private final JwtUtil jwtUtil;

  // 사용자 장바구니 생성
  @Operation(summary = "장바구니 생성", description = "사용자 첫 로그인 후 장바구니 생성")
  @PostMapping("/create")
  public ResponseEntity<?> createCart(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      // 서비스 메서드 호출
      String result = cartService.createCart(email);

      if ("장바구니가 정상적으로 생성되었습니다.".equals(result)) {
        return ResponseEntity.ok(result);
      } else {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
      }
    } catch (Exception e) {
      log.error("Error occurred while creating cart: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error occurred creating cart: " + e.getMessage());
    }
  }

  @Operation(summary = "장바구니에 상품 등록", description = "사용자의 장바구니에 상품 등록")
  @PostMapping("/add-product")
  public ResponseEntity<?> addProduct(
      @RequestHeader("Authorization") String bearerToken, @RequestBody AddProductRequest request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String loginId = claims.getId(); // 로그인 아이디

      String result = cartService.addProduct(loginId, request);

      if ("장바구니에 상품이 정상적으로 등록되었습니다.".equals(result)) {
        return ResponseEntity.ok(result);
      } else {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
      }
    } catch (RuntimeException e) {
      log.error("Error during add product in controller /api/cart/add-product: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "장바구니 상품 조회", description = "장바구니에 등록된 상품 조회")
  @GetMapping("/my-product")
  public ResponseEntity<?> getCartProduct(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      List<ProductResponse> response = cartService.getCartProducts(email);
      if (response == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("장바구니에 상품이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(response);
      }
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching cart product in controller /api/cart/my-product: {}",
          e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
