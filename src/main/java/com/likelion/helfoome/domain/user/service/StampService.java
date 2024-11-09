package com.likelion.helfoome.domain.user.service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.user.dto.StampResponse;
import com.likelion.helfoome.domain.user.entity.Stamp;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.StampRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StampService {

  private final StampRepository stampRepository;
  private final UserRepository userRepository;

  // 일단 기본 스탬프 생성
  public void createStamp(String email) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    Stamp stamp = new Stamp();
    stamp.setUser(user);
    stampRepository.save(stamp);
  }

  public void editStamp(User user) {
    Stamp stamp = stampRepository.findByUser_Email(user.getEmail()).orElseThrow();
    LocalDate today = LocalDate.now();
    DayOfWeek dayOfWeek = today.getDayOfWeek();
    switch (dayOfWeek) {
      case MONDAY:
        stamp.setMon(true);
        break;
      case TUESDAY:
        stamp.setTue(true);
        break;
      case WEDNESDAY:
        stamp.setWed(true);
        break;
      case THURSDAY:
        stamp.setThu(true);
        break;
      case FRIDAY:
        stamp.setFri(true);
        break;
      case SATURDAY:
        stamp.setSat(true);
        break;
      case SUNDAY:
        stamp.setSun(true);
        break;
    }
  }

  public StampResponse getStamp(String email) {
    Integer count = 0;
    Stamp stamp = stampRepository.findByUser_Email(email).orElseThrow();
    StampResponse response = new StampResponse();

    response.setMon(stamp.getMon());
    if (stamp.getMon() != null && stamp.getMon()) {
      count++;
    }

    response.setTue(stamp.getTue());
    if (stamp.getTue() != null && stamp.getTue()) {
      count++;
    }

    response.setWed(stamp.getWed());
    if (stamp.getWed() != null && stamp.getWed()) {
      count++;
    }

    response.setThu(stamp.getThu());
    if (stamp.getThu() != null && stamp.getThu()) {
      count++;
    }

    response.setFri(stamp.getFri());
    if (stamp.getFri() != null && stamp.getFri()) {
      count++;
    }

    response.setSat(stamp.getSat());
    if (stamp.getSat() != null && stamp.getSat()) {
      count++;
    }

    response.setSun(stamp.getSun());
    if (stamp.getSun() != null && stamp.getSun()) {
      count++;
    }

    response.setWeekStampCount(count);
    return response;
  }
}
