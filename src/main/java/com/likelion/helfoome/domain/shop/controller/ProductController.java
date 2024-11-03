package com.likelion.helfoome.domain.shop.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.helfoome.domain.shop.dto.ProductRequest;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequestDto) {
    if (productRequestDto.getImages() == null || productRequestDto.getImages().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      Product product = productService.createProduct(productRequestDto);
      return new ResponseEntity<>(product, HttpStatus.CREATED);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
