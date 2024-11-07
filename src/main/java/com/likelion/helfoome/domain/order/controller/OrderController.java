package com.likelion.helfoome.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.order.dto.OrderRequest;
import com.likelion.helfoome.domain.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "주문 하기", description = "주문버튼 누르면 이거 호출/ PIN 리턴합니다")
  @PostMapping("/create")
  public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
    String PIN = orderService.createOrder(orderRequest);
    return new ResponseEntity<>(PIN, HttpStatus.OK);
  }

  @Operation(summary = "구매확정", description = "사장님이 누르는 구매확정 버튼")
  @PatchMapping("/confirm")
  public ResponseEntity<String> confirmOrder(@RequestParam Long postId) {
    orderService.confirmOrder(postId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "주문 취소", description = "사장님이 누르는 구매 취소 버튼")
  @PatchMapping("/discard")
  public ResponseEntity<String> discardOrder(@RequestParam Long postId) {
    orderService.discardOrder(postId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
