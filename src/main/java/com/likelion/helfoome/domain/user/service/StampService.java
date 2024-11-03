package com.likelion.helfoome.domain.user.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.user.repository.StampRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StampService {
  private final StampRepository stampRepository;
}
