package com.likelion.helfoome.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.user.dto.StampResponse;
import com.likelion.helfoome.domain.user.service.StampService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/stamp")
public class StampController {

  private final JwtUtil jwtUtil;
  private final StampService stampService;

  @Operation(summary = "스탬프 가져오기", description = "요일별 스탬프 여부 반환")
  @GetMapping
  public ResponseEntity<StampResponse> getStamp(
      @RequestHeader("Authorization") String bearerToken) {

    String token = bearerToken.substring(7);
    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    String email = claims.getId();
    StampResponse stampResponse = stampService.getStamp(email);

    return new ResponseEntity<>(stampResponse, HttpStatus.OK);
  }
}
