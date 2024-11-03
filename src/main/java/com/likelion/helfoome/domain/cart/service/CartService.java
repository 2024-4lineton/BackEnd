package com.likelion.helfoome.domain.cart.service;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.cart.repository.CartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;
}
