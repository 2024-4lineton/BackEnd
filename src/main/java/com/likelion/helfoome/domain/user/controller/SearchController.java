package com.likelion.helfoome.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.post.dto.PostResponse;
import com.likelion.helfoome.domain.shop.dto.product.MainProductResponse;
import com.likelion.helfoome.domain.user.dto.SearchResponse;
import com.likelion.helfoome.domain.user.service.SearchService;
import com.likelion.helfoome.global.auth.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
  private final SearchService searchService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "검색 기록 조회", description = "최근 검색어")
  @GetMapping("/history")
  public ResponseEntity<?> getSearchByEmail(@RequestHeader("Authorization") String bearerToken) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      List<SearchResponse> responses = searchService.getSearchByEmail(email);

      if (responses == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("최근 검색어가 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(responses);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching email in controller /api/keyword: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "상품 키워드로 검색", description = "키워드를 통한 상품 검색")
  @GetMapping("/product/keyword")
  public ResponseEntity<?> getProductByEmail(
      @RequestHeader("Authorization") String bearerToken, @RequestParam String keyword) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      List<MainProductResponse> responses = searchService.searchProductByKeyword(keyword);
      searchService.createSearch(email, keyword);

      if (responses == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("키워드 상품이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(responses);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching keyword in controller /api/keyword: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "게시글 키워드로 검색", description = "키워드를 통한 게시글 검색")
  @GetMapping("/post/keyword")
  public ResponseEntity<?> getPostByEmail(
      @RequestHeader("Authorization") String bearerToken, @RequestParam String keyword) {
    try {
      String token = bearerToken.substring(7);
      Claims claims = jwtUtil.getAllClaimsFromToken(token);
      String email = claims.getId();

      List<PostResponse> responses = searchService.searchPostByKeyword(keyword);
      searchService.createSearch(email, keyword);

      if (responses == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("키워드 게시글이 존재하지 않습니다.");
      } else {
        return ResponseEntity.ok(responses);
      }
    } catch (RuntimeException e) {
      log.error("Error during fetching keyword in controller /api/post: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "검색 기록 삭제", description = "최근 검색어 검색 기록 삭제")
  @DeleteMapping("/history")
  public ResponseEntity<?> deleteSearchHistory(@RequestParam Long id) {
    try {
      String result = searchService.deleteSearchHistory(id);
      if (result.equals("검색 기록이 성공적으로 삭제되었습니다.")) {
        return ResponseEntity.ok(result);
      } else {
        return ResponseEntity.badRequest().body(result);
      }
    } catch (RuntimeException e) {
      log.error("Error during delete search history in controller /api/search: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
