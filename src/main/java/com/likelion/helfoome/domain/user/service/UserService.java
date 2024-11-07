package com.likelion.helfoome.domain.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.user.dto.UserResponse;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  // 가입된 사용자 전체 조회
  public List<UserResponse> getAllUsers() {
    // UserListResponse를 담을 리스트 생성
    List<UserResponse> responses = new ArrayList<>();
    UserResponse response = new UserResponse();

    List<User> users = userRepository.findAll();
    for (User user : users) {
      response = new UserResponse(user.getEmail(), user.getNickname(), user.getUserRole());
      responses.add(response);
    }

    return responses;
  }
}
