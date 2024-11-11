package com.likelion.helfoome.domain.post.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.post.dto.PostRequest;
import com.likelion.helfoome.domain.post.dto.PostResponse;
import com.likelion.helfoome.domain.post.service.PostService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
  private final PostService postService;
  private final JwtUtil jwtUtil;

  @Operation(
      summary = "글쓰기",
      description = "Parameter:(article | community | demand | supply), Token 사용자 Header로 받아 글 작성")
  @PostMapping("/{postType}")
  public ResponseEntity<?> createPost(
      @PathVariable String postType,
      @RequestHeader("Authorization") String bearerToken,
      @ModelAttribute PostRequest request) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      String result = postService.createPost(postType, email, request);

      if ("글 등록 중 오류가 발생했습니다.".equals(result)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("글이 등록되지 않았습니다.");
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("글 등록 중 오류 발생: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("글 형식 오류 발생: " + e.getMessage());
    }
  }

  @Operation(summary = "게시물 전체 조회", description = "Post Type으로 게시물 전체 조회")
  @GetMapping("/{postType}")
  public ResponseEntity<?> getAllPost(@PathVariable String postType) {
    try {
      List<PostResponse> postResponses = postService.getAllPost(postType);
      if (postResponses.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("게시물이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(postResponses);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching post list in controller /api/post: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "게시물 단일 조회", description = "Post별 id로 게시물 조회")
  @GetMapping("/{postType}/postId")
  public ResponseEntity<?> getPostById(@PathVariable String postType, @RequestParam Long postId) {
    try {
      PostResponse postResponse = postService.getPostById(postType, postId);
      if (postResponse == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("게시물이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(postResponse);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching post in controller /api/post/id: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "사용자 게시물 전체 조회", description = "Post Type으로 사용자 게시물(내가 쓴 글) 전체 조회")
  @GetMapping("/{postType}/email")
  public ResponseEntity<?> getAllPost(
      @PathVariable String postType, @RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      List<PostResponse> postResponses = postService.getAllPostByEmail(postType, email);
      if (postResponses.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("게시물이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(postResponses);
      }
    } catch (RuntimeException e) {
      log.error(
          "Error during fetching user's post list in controller /api/post: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "최신글 조회", description = "Post별 최신 게시물 상위 5개 조회")
  @GetMapping("/latest/{postType}")
  public ResponseEntity<?> getLatestPostById(@PathVariable String postType) {
    try {
      List<PostResponse> postResponses = postService.getLatestPosts(postType);
      if (postResponses.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("게시물이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(postResponses);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching post in controller /api/post/latest: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "인기글 조회", description = "Post별 인기 게시물 상위 5개 조회")
  @GetMapping("/hottest/{postType}")
  public ResponseEntity<?> getHottestPostById(@PathVariable String postType) {
    try {
      List<PostResponse> postResponses = postService.getHottestPosts(postType);
      if (postResponses.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("게시물이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(postResponses);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching post in controller /api/post/hottest: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
