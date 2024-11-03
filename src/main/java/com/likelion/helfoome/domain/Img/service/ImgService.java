package com.likelion.helfoome.domain.Img.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.Img.repository.ArticleImgRepository;
import com.likelion.helfoome.domain.Img.repository.CommunityImgRepository;
import com.likelion.helfoome.domain.Img.repository.ProductImgRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImgService {
  private final ArticleImgRepository articleImgRepository;
  private final CommunityImgRepository communityImgRepository;
  private final ProductImgRepository productImgRepository;
}
