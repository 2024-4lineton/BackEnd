package com.likelion.helfoome.domain.post.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.post.repository.ArticleLikeRepository;
import com.likelion.helfoome.domain.post.repository.CommunityLikeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
  private final ArticleLikeRepository articleLikeRepository;
  private final CommunityLikeRepository communityLikeRepository;
}
