package com.likelion.helfoome.global.Scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.helfoome.domain.user.repository.StampRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Scheduler {

  private final StampRepository stampRepository;

  @Scheduled(cron = "0 0 0 ? * SAT") // 매주 토요일 자정
  @Transactional
  public void resetStamps() {
    // 모든 Stamp 엔티티를 조회한 후 각 필드를 false로 설정
    stampRepository
        .findAll()
        .forEach(
            stamp -> {
              stamp.setMon(false);
              stamp.setTue(false);
              stamp.setWed(false);
              stamp.setThu(false);
              stamp.setFri(false);
              stamp.setSat(false);
              stamp.setSun(false);
            });
  }
}
