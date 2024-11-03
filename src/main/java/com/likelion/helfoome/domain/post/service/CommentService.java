package com.likelion.helfoome.domain.post.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.post.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
}
