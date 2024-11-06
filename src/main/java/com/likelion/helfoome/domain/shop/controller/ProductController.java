package com.likelion.helfoome.domain.shop.controller;

import com.likelion.helfoome.domain.shop.dto.product.ProductList;
import com.likelion.helfoome.domain.shop.dto.product.ProductRequest;
import com.likelion.helfoome.domain.shop.dto.product.ProductResponse;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "상품 등록", description = "상품 등록")
  @PostMapping("/new-product")
  public ResponseEntity<Product> createProduct(@ModelAttribute ProductRequest productRequestDto) {
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

  @Operation(summary = "상품 상세", description = "상품 상세")
  @GetMapping("/detail")
  public ResponseEntity<ProductResponse> getProductDetail(@RequestParam Long productId) {
    ProductResponse productResponse = productService.getProductDetail(productId);
    if (productResponse == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(productService.getProductDetail(productId), HttpStatus.OK);
  }

  @Operation(
      summary = "정렬된 상품 목록 가져오기",
      description =
          "sort 0, 1, 2면 각각 단거리, 최저가, 최고할인순 "
              + "/  shopType은 0, 1, 2 각각 전통, 골목시장, 브랜드) "
              + "/ page는 몇 페이지인지, size는 한 페이지 사이즈 어떻게 할건지"
              + "/marketName은 전통시장인 경우에만 시장 이름 넘겨주면 되고 아니면 걍 null값넣던 뭐 넣던 상관 없어요")
  @GetMapping("/productList")
  public ResponseEntity<ProductList> getProductList(
      @RequestParam String userAddr,
      @RequestParam Integer shopType,
      @RequestParam int sort,
      @RequestParam int page,
      @RequestParam int size,
      @RequestParam String marketName) {
    ProductList productList =
        productService.getSortedProductList(userAddr, shopType, sort, page, size, marketName);

    return new ResponseEntity<>(productList, HttpStatus.OK);
  }
}
