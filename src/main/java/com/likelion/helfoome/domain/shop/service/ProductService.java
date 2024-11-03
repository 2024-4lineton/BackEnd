package com.likelion.helfoome.domain.shop.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.shop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
}
