package com.likelion.helfoome.domain.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.order.dto.OrderCompleteList;
import com.likelion.helfoome.domain.order.service.OrderService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

  private final OrderService orderService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "주문 하기", description = "주문버튼 누르면 이거 호출/ PIN 리턴합니다")
  @PostMapping("/create")
  public ResponseEntity<String> createOrder(
      @RequestParam Long shopId,
      @RequestParam Long productId,
      @RequestHeader("Authorization") String bearerToken) {
    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String email = claims.getId();

    String PIN = orderService.createOrder(shopId, productId, email);
    return new ResponseEntity<>(PIN, HttpStatus.OK);
  }

  @Operation(
      summary = "이번달 구매 수",
      description = "이번달 구매 수 리턴 / 백엔드에서 현재 날짜 가져와서 지금이 어떤 달이고 뭐 이런거 다 계산합니다옹")
  @GetMapping("/countCurMonth")
  public ResponseEntity<Long> getCurMonthCount(@RequestHeader("Authorization") String bearerToken) {
    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String email = claims.getId();
    Long count = orderService.getOrdersCountForCurrentMonthAndUser(email);
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  @Operation(summary = "구매확정", description = "사장님이 누르는 구매확정 버튼")
  @PatchMapping("/confirm")
  public ResponseEntity<String> confirmOrder(@RequestParam Long orderId) {
    orderService.confirmOrder(orderId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "주문 취소", description = "사장님이 누르는 구매 취소 버튼")
  @PatchMapping("/discard")
  public ResponseEntity<String> discardOrder(@RequestParam Long orderId) {
    orderService.discardOrder(orderId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "주문 내역", description = "orderState 0이면 예약 완료, 1이면 주문 확정, 2면 주문 취소")
  @GetMapping("/history")
  public ResponseEntity<List<OrderCompleteList>> getOrderHistory(
      @RequestHeader("Authorization") String bearerToken) {
    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String email = claims.getId();

    List<OrderCompleteList> response = orderService.getOrderHistory(email);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
