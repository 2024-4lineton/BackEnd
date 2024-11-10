package com.likelion.helfoome.domain.user.controller;

import com.likelion.helfoome.domain.user.dto.NotificationList;
import com.likelion.helfoome.domain.user.service.NotificationService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

  private final JwtUtil jwtUtil;
  private final NotificationService notificationService;

  @Operation(summary = "알림목록 조회", description = "알림목록 조회")
  @GetMapping
  public ResponseEntity<List<NotificationList>> getNotices(
      @RequestHeader("Authorization") String bearerToken) {
    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String email = claims.getId();

    List<NotificationList> response = notificationService.getNotices(email);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
