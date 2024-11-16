package com.likelion.helfoome.global.distance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 테스트 용도로 만든 컨트로로러고 실제로 요청을 보내는 용도르는 사용하지 않을듯 합니다~
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/distance")
public class DistanceController {

  private final DistanceService distanceService;

  @GetMapping
  public ResponseEntity<Long> GetDistance(String userAddr, String shopAddr) {
    return ResponseEntity.ok(distanceService.getDistance(userAddr, shopAddr));
  }
}
