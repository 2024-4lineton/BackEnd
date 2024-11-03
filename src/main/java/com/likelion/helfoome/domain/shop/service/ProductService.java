package com.likelion.helfoome.domain.shop.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.likelion.helfoome.domain.Img.entity.ProductImg;
import com.likelion.helfoome.domain.Img.repository.ProductImgRepository;
import com.likelion.helfoome.domain.shop.dto.ProductRequest;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.global.S3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductImgRepository productImgRepository;
  private final ShopRepository shopRepository;
  private final S3Service s3Service;

  @Transactional
  public Product createProduct(ProductRequest productRequest) throws IOException {

    // Shop 엔티티 조회 (외래키 설정때매)
    Optional<Shop> shopOptional = shopRepository.findById(productRequest.getShopId());
    if (shopOptional.isEmpty()) {
      throw new IllegalArgumentException("Invalid shopID");
    }
    Shop shop = shopOptional.get();
    // Product 엔티티 생성 및 저장

    Product product = new Product();
    product.setShop(shop);
    product.setProductName(productRequest.getProductName());
    product.setDescription(productRequest.getDescription());
    product.setPrice(productRequest.getPrice());
    productRepository.save(product);

    // S3에 이미지 업로드 및 ProductImg 엔티티 생성
    for (MultipartFile image : productRequest.getImages()) {
      String imageUrl = s3Service.upload(image, "productImages");
      ProductImg productImg = new ProductImg();
      productImg.setProduct(product);
      productImg.setProductImageName(image.getOriginalFilename());
      productImg.setProductImageUrl(imageUrl);
      productImgRepository.save(productImg);
    }

    return product;
  }
}
