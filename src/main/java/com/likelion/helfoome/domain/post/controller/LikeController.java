package com.likelion.helfoome.domain.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.post.service.LikeService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/post/like")
public class LikeController {
  private final LikeService likeService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "좋아요 누르기", description = "Header로 받은 사용자로 좋아요 생성")
  @PostMapping("/{postType}/id")
  public ResponseEntity<?> createLike(
      @PathVariable String postType,
      @RequestParam Long id,
      @RequestHeader("Authorization") String bearerToken) {

    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      String result = likeService.createLike(postType, email, id);

      if ("좋아요 중 오류가 발생했습니다.".equals(result)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("좋아요에 실패했습니다.");
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("좋아요 중 오류 발생: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("좋아요 형식 오류 발생: " + e.getMessage());
    }
  }
}
