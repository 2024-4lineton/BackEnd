package com.likelion.helfoome.domain.shop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.helfoome.domain.Img.entity.ProductImg;
import com.likelion.helfoome.domain.Img.repository.ProductImgRepository;
import com.likelion.helfoome.domain.Img.service.ImgService;
import com.likelion.helfoome.domain.shop.dto.product.ProductInList;
import com.likelion.helfoome.domain.shop.dto.product.ProductList;
import com.likelion.helfoome.domain.shop.dto.product.ProductRequest;
import com.likelion.helfoome.domain.shop.dto.product.ProductResponse;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;
import com.likelion.helfoome.global.S3.service.S3Service;
import com.likelion.helfoome.global.distance.DistanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductImgRepository productImgRepository;
  private final ImgService imgService;
  private final ShopRepository shopRepository;
  private final S3Service s3Service;
  private final UserRepository userRepository;
  private final DistanceService distanceService;

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
    product.setDiscountPrice(productRequest.getDiscountPrice());
    product.setQuantity(productRequest.getQuantity());
    product.setDiscountPercent(product.getDiscountPercent());
    product.setIsSelling(true);
    // tlqkf가게 주소 아니고 다른주소로 정할수도 있다길래 이 부분 추가
    if (productRequest.getRealAddr() == null) {
      product.setRealAddr(shop.getShopAddr());
    } else {
      product.setRealAddr(productRequest.getRealAddr());
    }
    productRepository.save(product);
    // S3에 이미지 업로드 및 ProductImg 엔티티 생성
    imgService.uploadProductImg(productRequest.getImages(), product);
    return product;
  }

  public ProductResponse getProductDetail(Long productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid productID"));
    List<ProductImg> productImgs = productImgRepository.findByProductId(product.getId());
    List<String> productImgUrls = new ArrayList<>();
    for (ProductImg productImg : productImgs) {
      productImgUrls.add(productImg.getProductImageUrl());
    }

    ProductResponse productResponse = new ProductResponse();
    productResponse.setProductName(product.getProductName());
    productResponse.setDescription(product.getDescription());
    productResponse.setPrice(product.getPrice());
    product.setDiscountPrice(product.getDiscountPrice());
    productResponse.setQuantity(product.getQuantity());
    productResponse.setDiscountPercent(product.getDiscountPercent());
    productResponse.setIsSelling(false);
    productResponse.setImageUrls(productImgUrls);

    return productResponse;
  }

  public ProductList getSortedProductList(
      String userAddr, Integer shopType, int sort, int page, int pageSize, String marketName) {
    ProductList productList = getProductList(userAddr, shopType, marketName);

    // 정렬(sort 0, 1, 2면 각각 단거리, 최저가, 최고할인순)
    List<ProductInList> sortedList =
        productList.getProductInList().stream()
            .sorted(
                (p1, p2) -> {
                  switch (sort) {
                    case 0: // 단거리순
                      return p1.getDistance().compareTo(p2.getDistance());
                    case 1: // 최저가순 (discountPrice 기준)
                      return Integer.parseInt(p1.getDiscountPrice())
                          - Integer.parseInt(p2.getDiscountPrice());
                    case 2: // 최고할인순 (discountPercent 기준)
                      return Integer.parseInt(p2.getDiscountPercent().replace("%", ""))
                          - Integer.parseInt(p1.getDiscountPercent().replace("%", ""));
                    default:
                      throw new IllegalArgumentException("Invalid sort value: " + sort);
                  }
                })
            .collect(Collectors.toList());

    // 페이징해서 리턴을 하긴 해야하는데 tlqkf 제공해주는 Pagable을 쓸 수가 없잖아 이딴 조건에
    int start = page * pageSize;
    int end = Math.min(start + pageSize, sortedList.size());
    List<ProductInList> pagedList = sortedList.subList(start, end);

    ProductList sortedProductList = new ProductList();
    sortedProductList.setProductInList(pagedList);

    return sortedProductList;
  }

  public ProductList getProductList(String userAddr, Integer shopType, String marketName) {
    List<Shop> shops = new ArrayList<>();
    switch (shopType) {
      case 0:
        shops = shopRepository.findByMarketName(marketName);
        break;
      case 1:
      case 2:
        shops = shopRepository.findByShopType(shopType);
        break;
    }
    List<ProductInList> returnProducts = new ArrayList<>();
    for (Shop shop : shops) {
      List<Product> products;
      products = productRepository.findAllByShopId(shop.getId());

      for (Product product : products) {
        if (distanceService.getDistance(userAddr, product.getRealAddr()) <= 5000) {
          ProductInList productInList = new ProductInList();
          productInList.setProductId(product.getId());
          productInList.setShop(shop.getShopName());
          productInList.setProductName(product.getProductName());
          productInList.setPrice(product.getPrice());
          productInList.setDiscountPercent(product.getDiscountPercent());
          productInList.setQuantity(product.getQuantity());
          productInList.setDiscountPrice(product.getDiscountPrice());
          Long distance = distanceService.getDistance(userAddr, product.getRealAddr());
          productInList.setDistance(distance);
          productInList.setImgUrl(
              productImgRepository
                  .findByProductId(product.getId())
                  .getFirst()
                  .getProductImageUrl());

          returnProducts.add(productInList);
        }
      }
    }
    ProductList productList = new ProductList();
    productList.setProductInList(returnProducts);
    return productList;
  }
}
