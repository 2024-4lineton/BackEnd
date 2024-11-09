package com.likelion.helfoome.domain.shop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.helfoome.domain.order.entity.Order;
import com.likelion.helfoome.domain.order.repository.OrderRepository;
import com.likelion.helfoome.domain.shop.dto.product.OrderInList;
import com.likelion.helfoome.domain.shop.dto.product.ProductInList;
import com.likelion.helfoome.domain.shop.dto.product.ProductList;
import com.likelion.helfoome.domain.shop.dto.product.ProductManagingResponse;
import com.likelion.helfoome.domain.shop.dto.product.ProductRequest;
import com.likelion.helfoome.domain.shop.dto.product.ProductResponse;
import com.likelion.helfoome.domain.shop.dto.product.SellingProductInList;
import com.likelion.helfoome.domain.shop.dto.product.SellingProductList;
import com.likelion.helfoome.domain.shop.entity.Product;
import com.likelion.helfoome.domain.shop.entity.Shop;
import com.likelion.helfoome.domain.shop.repository.ProductRepository;
import com.likelion.helfoome.domain.shop.repository.ShopRepository;
import com.likelion.helfoome.domain.user.repository.UserInfoRepository;
import com.likelion.helfoome.global.S3.service.S3Service;
import com.likelion.helfoome.global.distance.DistanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ShopRepository shopRepository;
  private final OrderRepository orderRepository;
  private final UserInfoRepository userInfoRepository;
  private final S3Service s3Service;
  private final DistanceService distanceService;

  @Transactional
  public String createProduct(ProductRequest productRequest) throws IOException {

    // Shop 엔티티 조회
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
    product.setDiscountPercent(productRequest.getDiscountPercent());
    product.setIsSelling(true);
    if (productRequest.getRealAddr() == null) {
      product.setRealAddr(shop.getShopAddr());
    } else {
      product.setRealAddr(productRequest.getRealAddr());
    }
    product.setProductImageName(productRequest.getProductImg().getOriginalFilename());
    String imgUrl = s3Service.upload(productRequest.getProductImg(), "productImages");
    product.setProductImageURL(imgUrl);

    productRepository.save(product);

    return "상품이 성공적으로 등록되었습니다.";
  }

  // 상품상세
  public ProductResponse getProductDetail(Long productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid productID"));

    ProductResponse productResponse = new ProductResponse();
    productResponse.setProductName(product.getProductName());
    productResponse.setDescription(product.getDescription());
    productResponse.setPrice(product.getPrice());
    product.setDiscountPrice(product.getDiscountPrice());
    productResponse.setQuantity(product.getQuantity());
    productResponse.setDiscountPercent(product.getDiscountPercent());
    productResponse.setIsSelling(false);
    productResponse.setProductImgUrl(product.getProductImageURL());

    return productResponse;
  }

  public ProductList getSortedProductList(
      String email, Integer shopType, int sort, int page, int pageSize, String marketName) {
    String userAddr =
        userInfoRepository.findByUser_Email(email).orElseThrow().getActivityLocation();
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
                      return Integer.parseInt(p2.getDiscountPercent())
                          - Integer.parseInt(p1.getDiscountPercent());
                    default:
                      throw new IllegalArgumentException("Invalid sort value: " + sort);
                  }
                })
            .collect(Collectors.toList());

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
      products = productRepository.findByShopIdAndIsSellingTrue(shop.getId());

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
          productInList.setImgUrl(product.getProductImageURL());

          returnProducts.add(productInList);
        }
      }
    }
    ProductList productList = new ProductList();
    productList.setProductInList(returnProducts);
    return productList;
  }

  public ProductManagingResponse getProductManaging(Long productId) {

    ProductManagingResponse response = new ProductManagingResponse();
    Product product = productRepository.findById(productId).orElseThrow();
    List<OrderInList> returnOrders = new ArrayList<>();
    List<Order> orders = orderRepository.findByProductId(productId);
    response.setProduct(product);
    for (Order order : orders) {
      OrderInList orderInList = new OrderInList();
      orderInList.setOrderId(order.getId());
      orderInList.setNickname(order.getUser().getNickname());
      orderInList.setPIN(order.getPIN());
      orderInList.setOrderTime(order.getCreatedDate());
      returnOrders.add(orderInList);
    }
    response.setOrders(returnOrders);
    return response;
  }

  public SellingProductList getSellingProduct(Long shopId) {
    Shop shop = shopRepository.findById(shopId).orElseThrow();
    List<SellingProductInList> returnProducts = new ArrayList<>();
    List<Product> products;
    products = productRepository.findByShopIdAndIsSellingTrue(shopId);
    for (Product product : products) {
      SellingProductInList productInList = new SellingProductInList();
      productInList.setProductId(product.getId());
      productInList.setShop(shop.getShopName());
      productInList.setProductName(product.getProductName());
      productInList.setQuantity(product.getQuantity());
      productInList.setOrders(orderRepository.countByProductId(product.getId()));
      productInList.setImgUrl(product.getProductImageURL());

      returnProducts.add(productInList);
    }
    SellingProductList productList = new SellingProductList();
    productList.setProductInList(returnProducts);
    return productList;
  }

  public SellingProductList getUnSellingProduct(Long shopId) {
    Shop shop = shopRepository.findById(shopId).orElseThrow();
    List<SellingProductInList> returnProducts = new ArrayList<>();
    List<Product> products;
    products = productRepository.findByShopIdAndIsSellingFalse(shopId);
    for (Product product : products) {
      SellingProductInList productInList = new SellingProductInList();
      productInList.setProductId(product.getId());
      productInList.setShop(shop.getShopName());
      productInList.setProductName(product.getProductName());
      productInList.setQuantity(product.getQuantity());
      productInList.setOrders(orderRepository.countByProductId(product.getId()));
      productInList.setImgUrl(product.getProductImageURL());
      returnProducts.add(productInList);
    }
    SellingProductList productList = new SellingProductList();
    productList.setProductInList(returnProducts);
    return productList;
  }
}
