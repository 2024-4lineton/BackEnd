package com.likelion.helfoome.domain.post.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.post.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {
  private final ArticleRepository articleRepository;
}
