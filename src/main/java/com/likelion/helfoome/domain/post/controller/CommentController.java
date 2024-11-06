package com.likelion.helfoome.domain.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.post.dto.CommentRequest;
import com.likelion.helfoome.domain.post.service.CommentService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
  private final CommentService commentService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "댓글 작성", description = "Header로 받은 사용자로 댓글 작성")
  @PostMapping("/{postType}/postId")
  public ResponseEntity<?> createLike(
      @PathVariable String postType,
      @RequestParam Long postId,
      @RequestHeader("Authorization") String bearerToken,
      @RequestBody CommentRequest request) {

    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      String result = commentService.createComment(postType, email, postId, request);

      if ("댓글 작성 중 오류가 발생했습니다.".equals(result)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("댓글 작성에 실패했습니다.");
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("댓글 작성 중 오류 발생: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("댓글 형식 오류 발생: " + e.getMessage());
    }
  }
}
