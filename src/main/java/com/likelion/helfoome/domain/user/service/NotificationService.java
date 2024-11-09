package com.likelion.helfoome.domain.user.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.user.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository notificationRepository;
}
