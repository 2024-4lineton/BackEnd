package com.likelion.helfoome.global.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
  @Value("${spring.jwt.secret}")
  private String secretKey;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  // Access Token 발급
  public String createAccessToken(String email, String nickname) {
    Date expireTime = Date.from(Instant.now().plus(23, ChronoUnit.HOURS));
    Key key = getSigningKey();
    return Jwts.builder()
        .setId(email)
        .setSubject(nickname)
        .setIssuedAt(new Date())
        .setExpiration(expireTime)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // Refresh Token 발급
  public String createRefreshToken(String email) {
    Date expireTime = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));
    Key key = getSigningKey();
    return Jwts.builder()
        .setId(email)
        .setIssuedAt(new Date())
        .setExpiration(expireTime)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token, String email) {
    try {
      Claims claims =
          Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
      String extractedUsername = claims.getId();

      if (!extractedUsername.equals(email)) {
        log.info("JWT 토큰의 사용자 이름이 일치하지 않습니다.");
        return false;
      }
      if (claims.getExpiration().before(new Date())) {
        log.info("만료된 JWT 토큰입니다.");
        return false;
      }
      // 로그 더 안 봐도 될 때 주석처리
      log.info("JWT validation 성공");
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
      return false;
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
      return false;
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
      return false;
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
      return false;
    } catch (Exception e) {
      log.info("기타 오류 발생");
      return false;
    }
  }

  // Claims에서 모든 정보 추출
  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // 토큰에서 사용자 Email 추출
  public String extractEmail(String token) {
    return getAllClaimsFromToken(token).getId();
  }
}
